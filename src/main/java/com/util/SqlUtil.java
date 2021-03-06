package com.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlUtil {

    public static ResultSet executeSql(Connection conn, String sql) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.setQueryTimeout(20);
        return stmt.executeQuery(sql);
    }

    public static void ddl(Connection conn, String sql) {
        try {
            Statement stmt = conn.createStatement();
            stmt.setQueryTimeout(200);
            stmt.execute(sql);
        } catch (Exception e) {
            System.out.println("ddl fail");
            e.printStackTrace();
        }
    }

    public static String getDbString(String s) {
        return "'" + s + "'";
    }
}
