/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001-2003 UptoData, Inc.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by 
 *        UptoData Inc. (http://www.uptodata.com/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "UptoData" and "dbdoc" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission.  For written
 *    permission, please contact eitan@uptodata.com.
 *
 * 5. Products derived from this software may not be called "dbdoc" 
 *    or "uptodata", nor may "dbdoc" or "uptodata" appear in their 
 *    name, without prior written permission of UptoData Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE UPTODATA OR ITS CONTRIBUTORS 
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by Eitan
 * Suez on behalf of UptoData Inc.  For more information on UptoData, 
 * please see <http://www.uptodata.com/>.
 *
 */

package org.ashkelon.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

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
