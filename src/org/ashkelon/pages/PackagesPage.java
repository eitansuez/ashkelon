package org.ashkelon.pages;

import org.ashkelon.*;
import org.ashkelon.db.*;
import java.sql.*;
import java.util.*;

public class PackagesPage extends Page
{
    public PackagesPage()
    {
        super();
    }

    public String init() throws SQLException
    {
		// Poor man's cache - how do we do live updates to the database then?
        if (app.getAttribute("javaPkgs") != null)
        {
            return null;
        }

        List pkgList = getPackageList();
        HashMap javaPkgs = new HashMap();
        JPackage pkg = null;
        for (int i = 0; i < pkgList.size(); i++)
        {
            pkg = (JPackage) pkgList.get(i);
            int index = pkg.getName().indexOf('.');
            String packageBase = null;
            if (index < 0)
            {
                packageBase = " ";
            }
            else
            {
                packageBase = pkg.getName().substring(0, index);
            }
            if (javaPkgs.containsKey(packageBase))
            {
                List list = (List) javaPkgs.get(packageBase);
                list.add(pkg);
            }
            else
            {
                List list = new ArrayList();
                list.add(pkg);
                javaPkgs.put(packageBase, list);
            }
        }
        app.setAttribute("javaPkgs", javaPkgs);
        return null;
    }

    public List getPackageList() throws SQLException
    {
        String sql = DBMgr.getInstance().getStatement("getpackages2");

        Statement stmt = conn.createStatement();
        ResultSet rset = stmt.executeQuery(sql);

        List pkgList = new ArrayList();

        JPackage pkg;
        while (rset.next())
        {
            pkg = new JPackage(rset.getString(2));
            pkg.setId(rset.getInt(1));
            pkg.setDoc(
                new DocInfo(
                    rset.getString(3),
                    rset.getString(4),
                    rset.getString(5)));
            pkgList.add(pkg);
        }

        rset.close();
        stmt.close();

        return pkgList;
    }

}
