package org.ashkelon.manager;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;
import java.io.*;

/**
 * @author Eitan Suez
 */
public class AshkelonTask extends Task
{
    private String cmd;
    private String apifile = "";

    /*
     * <ashkelon cmd="add" apifile="someapi.xml">
     *  <arg name="source" value="1.4" />
     * </ashkelon>
     */
    
    public void setCmd(AshkelonCmd cmd)
    {
      this.cmd = cmd.getValue();
    }
    public void setApifile(File file)
    {
      this.apifile = file.getAbsolutePath();
      log("api file is: "+apifile);
    }
    
    public void execute()
    {
      if ("add".equals(cmd))
      {
        String[] args = new String[1];
        args[0] = apifile;
        org.ashkelon.manager.AshkelonCmd.addApiXmlCmd(args);
      }
    }
    
    public static class AshkelonCmd extends EnumeratedAttribute
    {
        public String[] getValues()
        {
            return new String[] {"add", "reset", "list", "remove", "updaterefs"};
        }
    }
}
