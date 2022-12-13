package com.wms.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
        final static String URL = "jdbc:mysql://localhost:3306/wms";
        final static String USER = "wms_admin";
        final static String PASS = "P@ssword12";
        public static Connection connect() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASS);
        }
}
