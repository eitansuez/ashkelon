package org.ashkelon.pages;

public class CommandInfo
{
   private String command;
   private String pageName;
   private String className;
   private String caption;
   private String tableName;
   private boolean inTrail;

   public CommandInfo()
   {
      command = "";
      pageName = "";
      className = "";
      inTrail = false;
   }

   public String getCommand() { return command; }
   public void setCommand(String command) { this.command = command; }

   public String getPageName() { return pageName; }
   public void setPageName(String pageName) { this.pageName = pageName; }

   public String getClassName() { return className; }
   public void setClassName(String className) { this.className = className; }

   public String getCaption() { return caption; }
   public void setCaption(String caption) { this.caption = caption; }

   public String getTableName() { return tableName; }
   public void setTableName(String tableName) { this.tableName = tableName; }
   
   public boolean getInTrail() { return inTrail; }
   public boolean isInTrail() { return inTrail; }
   public void setInTrail(boolean inTrail) { this.inTrail = inTrail; }

}
