/*
 * Created on Apr 20, 2005
 */
package org.ashkelon.manager;

import java.io.File;

/**
 * @author Eitan Suez
 */
public interface IRepository
{
   public void checkout(File basepath, Repository repository);
   public void update(File basepath, Repository repository);
   public String sourcepath(Repository r);
}