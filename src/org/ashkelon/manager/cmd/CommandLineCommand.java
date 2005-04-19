/*
 * Created on Apr 18, 2005
 */
package org.ashkelon.manager.cmd;

import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;

/**
 * @author Eitan Suez
 */
public interface CommandLineCommand
{
   public String getName();
   public String getDescription();
   public String getExample();
   public String getNote();
   
   public void registerParameters() throws JSAPException;
   public void invoke(JSAPResult result);
   
   public void printUsage(JSAPResult result);
}
