/**********************************************************************
Copyright (c) 1998 - 2003 Bob Hays and others.
All rights reserved.   This program and the accompanying materials
are made available under the terms of the Common Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/cpl-v10.html

Contributors:
    Bob Hays, Computer Geek
**********************************************************************/
package org.ashkelon.ant;

import java.io.*;
import java.util.*;

import org.apache.tools.ant.*;
import org.apache.tools.ant.taskdefs.*;
import org.apache.tools.ant.types.*;

import org.ashkelon.*;
import org.ashkelon.util.*;

/**
 * This is an ant task to run ashkelon, a JavaDoc-in-a-database toolkit.
 * 
 * @author <a href="mailto:electricbob@alephnaught.com">Bob Hays, Computer Geek</a>
 * @version 0.1
 */
public class AshkelonTask extends Javadoc
{
	/**
	 * Default constructor
	 */
	public AshkelonTask()
	{
	   super();
	}

	/**
	 * Overrides the execute() method in org.apache.tools.ant.Task to run ashkelon.
	 * 
	 * @see org.apache.tools.ant.Task#execute()
	 */
	public void execute() throws BuildException
	{
		Logger log = Logger.getInstance();
		log.setPrefix(_PROGRAM_NAME);
		if (_verbose)
		{
			log.setTraceLevel(Logger.VERBOSE);
		}
		if (_debug)
		{
			log.setTraceLevel(Logger.DEBUG);
		}
		try
		{
			_parameterCheck();
			if (_operation.compareTo(_RESET_OPERATION) == 0)
			{
				Ashkelon.resetCmd();
			}
			else if (_operation.compareTo(_LIST_OPERATION) == 0)
			{
				Ashkelon.listCmd();
			}
			else if (_operation.compareTo(_REMOVE_OPERATION) == 0)
			{
				String[] includes = _includePatterns.getIncludePatterns(getProject());
				Ashkelon.removeCmd(includes);
			}
			else if (_operation.compareTo(_UPDATEREFS_OPERATION) == 0)
			{
				Ashkelon.updateRefsCmd();
			}
			else if (_operation.compareTo(_ADD_OPERATION) == 0)
			{
                           String[] args = {"-api", "@"+_descriptionFile.getCanonicalPath()};
		  	   Ashkelon.addapiCmd(args);
			}
		}
		catch (BuildException bex)
		{
			throw bex;
		}
		catch (Exception ex)
		{
			ex.printStackTrace(log.getWriter());
			throw new BuildException(
				_PROGRAM_NAME + " task threw an exception",
				ex);
		}
	}

	public void setApi(File f) throws BuildException
	{
	   _descriptionFile = f;
	}

	public void setOperation(String op)
	{
	   _operation = op;
	}

	public void setVerbose(boolean v)
	{
	   _verbose = v;
	}

	public void setDebug(boolean d)
	{
	   _debug = d;
	}

	/**
	 * Add a name entry on the include list
	 */
	public PatternSet.NameEntry createInclude()
	{
		return _includePatterns.createInclude();
	}

	public PatternSet getIncludes()
	{
		return _includePatterns;
	}
	/**
	 * Tests that the operation attribute has a value.
	 * 
	 * @return true indicates that the attribute has a value.
	 */
	private boolean _isOperationSet()
	{
		return !(_operation == null);
	}

	/**
	 * Tests that the descriptionfile attribute has a value.
	 * 
	 * @return true indicates that the attribute has a value.
	 */
	private boolean _isDescriptionFileSet()
	{
		return !(_descriptionFile == null);
	}

	/**
	 * Checks that all parameter inputs are valid before doing any real work.
	 * 
	 * @throws BuildException
	 */
	private void _parameterCheck() throws BuildException
	{
		// Check that operation attribute has an appropriate value
		if (_isOperationSet())
		{
			boolean match = false;
			for (int i = 0; i < _VALID_OPERATIONS.length; i++)
			{
				if (_operation.compareTo(_VALID_OPERATIONS[i]) == 0)
				{
					match = true;
					break;
				}
			}
			if (!match)
			{
				throw new BuildException(
					_PROGRAM_NAME
						+ " task valid operations are "
						+ _convertArrayToString(_VALID_OPERATIONS));
			}
		}
		else
		{
			throw new BuildException(
				_PROGRAM_NAME
					+ " task requires the operation attribute; valid operations are "
					+ _convertArrayToString(_VALID_OPERATIONS));
		}
		if (_operation.compareTo(_ADD_OPERATION) == 0)
		{
			if (_isDescriptionFileSet())
			{
				if (!_descriptionFile.exists())
				{
					throw new BuildException(
						_PROGRAM_NAME
							+ " task cannot find api file "
							+ _descriptionFile);
				}
			}
			else
			{
				throw new BuildException(_PROGRAM_NAME + " task requires the api attribute, which is the XML file to describe the project");
			}
		}
		else if (_operation.compareTo(_REMOVE_OPERATION) == 0)
		{
			String[] list = _includePatterns.getIncludePatterns(getProject());
			if (list.length <= 0)
			{
				throw new BuildException(_PROGRAM_NAME + " task operation remove requires an include list");
			}
		}
	}

	private String _convertArrayToString(String[] a)
	{
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < a.length; i++)
		{
			if (i != 0)
			{
				buffer.append(", ");
			}
			buffer.append(a[i]);
		}
		return buffer.toString();
	}

	private static final String _PROGRAM_NAME = "Ashkelon";
	private static final String _ADD_OPERATION = "add";
	private static final String _RESET_OPERATION = "reset";
	private static final String _LIST_OPERATION = "list";
	private static final String _REMOVE_OPERATION = "remove";
	private static final String _UPDATEREFS_OPERATION = "updaterefs";

	private static final String[] _VALID_OPERATIONS =
		{
			_RESET_OPERATION,
			_LIST_OPERATION,
			_REMOVE_OPERATION,
			_ADD_OPERATION,
			_UPDATEREFS_OPERATION };

	private boolean _verbose = false;
	private boolean _debug = false;
	private String _operation = null;
	private String _extdirs = null;
	private File _descriptionFile = null;
	private PatternSet _includePatterns = new PatternSet();
}
