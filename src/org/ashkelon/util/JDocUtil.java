package org.ashkelon.util;
/**
 *  Copyright UptoData Inc. 2001
 *  March 2001
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ashkelon.DocInfo;
import org.ashkelon.pages.ConfigInfo;

import com.sun.javadoc.ParamTag;
import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.Tag;
import com.sun.javadoc.Type;

/**
 * A class containing various utility methods for dealing with the javadoc object 
 * model com.sun.javadoc
 *
 * @author Eitan Suez
 */
public class JDocUtil
{
   public static final int UNKNOWN_TYPE = -123;
   
   /** programElement accessibility constants */
   public static final int PUBLIC = 1;
	public static final int PROTECTED = 2;
	public static final int PRIVATE = 3;
	public static final int PACKAGEPRIVATE = 4;

	public static final String[] ACCESSIBILITIES = {"public", "protected", "private", "packageprivate"};
   
   /**
    * @param dim number of array dimensions
    * @return "[]" x dim (in perl speak)
    */
   public static String getDimension(int dim)
   {
      return StringUtils.join("[]", "", dim);
   }
   
   /**
    * @return number of array dimensions for Type
    * @param type a javadoc type
    */
   public static int getDimension(Type type)
   {
      if (type.dimension() == null) return 0;
      return (type.dimension().length()/2);
   }

   /**
    * @param qualifiedName qualified name of a program element doc
    * @return unqualified portion of qualifiedName
    */
   public static String unqualify(String qualifiedName)
   {
      return unqualify(qualifiedName, false);
   }
   
   public static String unqualify(String qualifiedName, boolean isNestedClass)
   {
      if (qualifiedName == null) { return ""; }
      
      int idx = qualifiedName.indexOf("(");
      boolean isexecmember = (idx > -1);
      if (isexecmember)
      {
         String end = qualifiedName.substring(idx);
         String start = qualifiedName.substring(0,idx);
         int index = start.lastIndexOf(".");
         if (index < 0) { return qualifiedName; }
         return start.substring(index+1) + end;
      }
      else
      {
         int index = qualifiedName.lastIndexOf(".");
         if (index < 0) { return qualifiedName; }

         if (isNestedClass)
         {
            String firstPart = qualifiedName.substring(0, index);
            index = firstPart.lastIndexOf(".");
            if (index < 0) { return qualifiedName; }
         }
         
         return qualifiedName.substring(index+1);
      }
   }

   /**
    * potential problems with this method: e.g. pkgName = java.awt
    * and name = java.awt.event.MouseMotionListener
    * even though name is in different package, the java.awt. part
    * will be truncated.  need to rethink this.
    */
   public static String conditionalQualify(String name, String pkgName)
   {
      if (name.startsWith(pkgName+"."))
      {
         return name.substring(pkgName.length()+1);
      }
      else if (name.startsWith("java.lang."))
      {
         return name.substring("java.lang.".length());
      }
      else
      {
         return name;
      }
   }
   
   public static boolean isQualified(String name)
   {
      return (name.indexOf(".")>=0);
   }
   
   /**
    * returns the contents of a sequence of tag objects as 
    * one text string
    */
   public static String getTagText(Tag tags[])
   {
      String text = "";
      for (int i=0; i<tags.length; i++)
      {
         text += tags[i].text() + " ";
      }
      return text.trim();
   }
   
   /**
    * resolves all inline tags in a description into html links
    * @param tags text represented as an array of tags, as returned by doc.inlineTags()
    * @return resolved text
    */
   public static String resolveDescription(DocInfo sourcedoc, Tag tags[])
   {
      return ConfigInfo.getInstance().getResolver().resolveDescription(sourcedoc, tags);
   }
   
   /**
    * returns a list of tags as an array of text strings
    * (assumes that within one tag's text, there may be > 1 entry
    *  where entries are separated with a comma -- the author tag
    *  behaves this way)
    */
   public static String[] getTagList(Tag tags[])
   {
      List list = new ArrayList();
      for (int i=0; i<tags.length; i++)
      {
         String[] vals = StringUtils.split(tags[i].text(), ",");
         for (int j=0; j<vals.length; j++)
         {
            list.add(vals[j]);
         }
      }
      String[] stringlist = new String[list.size()];
      list.toArray(stringlist);
      return stringlist;
   }
   
   /**
    * @return whether type is a primitive type (int, short, etc..)
    */
   public static boolean isPrimitive(Type type)
   {
      return (type.asClassDoc() == null);
   }
   
   /**
    * @return accessibility of programElement (PUBLIC, PRIVATE, etc..)
    */
   public static int getAccessibility(ProgramElementDoc programElement)
   {
      if (programElement.isPublic())
      {
         return PUBLIC;
      }
      else if (programElement.isProtected())
      {
         return PROTECTED;
      }
      else if (programElement.isPrivate())
      {
         return PRIVATE;
      }
      else
      {
         return PACKAGEPRIVATE;
      }
   }
   
   /**
    * @return parameter name to comment map
    */
    public static Map makeParamMap(DocInfo sourcedoc, ParamTag[] paramTags)
    {
       Map paramInfo = new HashMap();
       String paramdescription = "";
       for (int i=0; i<paramTags.length; i++)
       {
          //paramInfo.put(paramTags[i].parameterName(), paramTags[i].parameterComment());
          paramdescription = resolveDescription(sourcedoc, paramTags[i].inlineTags());
          paramInfo.put(paramTags[i].parameterName(), paramdescription);
       }
       return paramInfo;
    }
    
    /**
     * provides ability to specify package list to process indirectly by providing
     * file name that contains a list of packages sited one per line
     * (as in javadoc @pkgnamesfile)
     */
    public static List getPackageListFromFileName(String filename)
    {
       List pkgs = new ArrayList(12);
       if (filename==null) return pkgs;
       if (filename.startsWith("@"))
          filename = filename.substring(1);
       try
       {
          BufferedReader br = new BufferedReader(new FileReader(filename));
          String pkg = null;
          while ((pkg=br.readLine()) != null)
          {
             pkgs.add(pkg);
          }
       }
       catch (IOException ex)
       {
       }
       return pkgs;
    }
    
}
