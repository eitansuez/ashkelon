package org.ashkelon.util;

import java.util.*;

public class TreeNode
{
   private TreeNode parent;
   private Map children;
   private Object value;
   
   public TreeNode()
   {
      parent = null;
      children = new TreeMap();
   }
   
   public TreeNode(Object value)
   {
      this();
      setValue(value);
   }
   
   public void setValue(Object value)
   {
      this.value = value;
   }
   public Object getValue()
   {
      return value;
   }
   
   public TreeNode getParent()
   {
      return parent;
   }
   public void setParent(TreeNode parent)
   {
      this.parent = parent;
   }
   
   public TreeNode getChild(String key)
   {
      return (TreeNode) children.get(key);
   }
   
   public void addChild(String key, TreeNode child)
   {
      child.setParent(this);
      children.put(key, child);
   }
   
   public TreeNode getOnlyChild()
   {
      Iterator itr = children.values().iterator();
      if (itr.hasNext())
        return (TreeNode) children.values().iterator().next();
      else
        return null;
   }
   
   public Map getChildren()
   {
      return children;
   }
   public void setChildren(Map children)
   {
      this.children = new TreeMap(children);
   }
   
   public boolean isEmpty()
   {
      return (getChildren().values().size() == 0);
   }
   
   
   public static String printTree(TreeNode node, int level)
   {
      Object value = node.getValue();
      if (value==null) return "";
      
      String caption = value.toString();
      String indent = StringUtils.join("   ","",level);
      String output = indent+caption+" ("+level+")\n";
      
      Map children = (Map) node.getChildren();
      Collection values = children.values();
      Iterator itr = values.iterator();
      TreeNode childNode;
      while (itr.hasNext())
      {
         childNode = (TreeNode) itr.next();
         output += printTree(childNode, level + 1);
      }
      
      return output;
   }
}
