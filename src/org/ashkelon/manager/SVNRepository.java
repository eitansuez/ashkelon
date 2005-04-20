/*
 * Created on Apr 20, 2005
 */
package org.ashkelon.manager;

import java.io.File;

import org.ashkelon.util.Logger;
import org.ashkelon.util.StringUtils;
import org.tmatesoft.svn.core.ISVNWorkspace;
import org.tmatesoft.svn.core.SVNWorkspaceManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.ws.fs.FSEntryFactory;
import org.tmatesoft.svn.core.io.SVNException;
import org.tmatesoft.svn.core.io.SVNRepositoryLocation;

/**
 * @author Eitan Suez
 */
public class SVNRepository implements IRepository
{
   private static SVNRepository _instance = null;
   
   public static SVNRepository getInstance()
   {
      if (_instance == null)
         _instance = new SVNRepository();
      return _instance;
   }
   
   Logger log = Logger.getInstance();
   
   private SVNRepository()
   {
      DAVRepositoryFactory.setup();
      FSEntryFactory.setup();
   }

   
   private String path(File basepath, Repository r)
   {
      // path is: basepath + modulename
      // e.g. /tmp/sourcecache/jakarta/bcel
      return basepath.getAbsolutePath() + File.separator + r.getModulename();
   }
   
   private ISVNWorkspace workspace(File basepath, Repository r) throws SVNException
   {
      // this will create the necessary subdirectories if not present
      return SVNWorkspaceManager.createWorkspace("file", path(basepath, r));
   }
   
   public void checkout(File basepath, Repository r)
   {
      try
      {
         ISVNWorkspace workspace = workspace(basepath, r);
         
         String[] sourcepaths = StringUtils.split(r.getSourcepath(), ":");
         
         for (int i=0; i<sourcepaths.length; i++)
         {
            
            // baseurl + modulename + tagname + srcpath
            // e.g.: "http://svn.apache.org/repos/asf/jakarta/bcel/trunk/src/java";
            String url = r.getUrl() + "/" + r.getModulename() + "/" + 
              r.getTagname() + "/" + sourcepaths[i];
            log.traceln("checking out from svn url: "+url);
            
            SVNRepositoryLocation location = SVNRepositoryLocation.parseURL(url);
            long revision = workspace.checkout(location, ISVNWorkspace.HEAD, false);
            log.debug("checked out revision: "+revision);
         
         }

      }
      catch (SVNException ex)
      {
         log.error("svn checkout failed!");
         log.error("SVNException: "+ex.getMessage());
      }
   }
   
   /*
    * this needs some work.
    * 
    * for one thing if i change tagnames, i need to do an svn switch, which
    *  i'm not doing at the moment
    */
   public void update(File basepath, Repository r)
   {
      try
      {
         ISVNWorkspace workspace = workspace(basepath, r);

         String[] sourcepaths = StringUtils.split(r.getSourcepath(), ":");
         
         for (int i=0; i<sourcepaths.length; i++)
         {
            // baseurl + modulename + tagname + srcpath
            // e.g.: "http://svn.apache.org/repos/asf/jakarta/bcel/trunk/src/java";
   
            String url = r.getUrl() + "/" + r.getModulename() + "/" + 
              r.getTagname() + "/" + sourcepaths[i];
            
            log.traceln("updating from svn url: "+url);
            
            SVNRepositoryLocation location = SVNRepositoryLocation.parseURL(url);
            
            // updating..
            workspace.update(location, path(basepath, r), ISVNWorkspace.HEAD, true);
         }
         
      }
      catch (SVNException ex)
      {
         log.error("svn update failed!");
         log.error("SVNException: "+ex.getMessage());
      }
   }
   
   // with svn you can check out from multiple places into the
   // same root/base directory, so after checkout your local
   // workspace is all set
   public String sourcepath(Repository r)
   {
      return sourcepath(r.getModulename());
   }
   public String sourcepath(String modulename)
   {
      return Config.getInstance().getSourcePathBase() + File.separator + modulename;
   }
}
