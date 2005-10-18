package org.ashkelon;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: eitan
 * Date: Sep 9, 2005
 * Time: 12:39:01 PM
 */
public interface Persistable
{
   public String key();
   public String getQualifiedName();
   public int getId();
   public void setId(int id);
   public boolean isResolved();
   public void store(Connection conn) throws SQLException;
}
