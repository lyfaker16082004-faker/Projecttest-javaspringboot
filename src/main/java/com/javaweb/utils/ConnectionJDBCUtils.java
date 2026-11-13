package com.javaweb.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionJDBCUtils {
	static final String DB_URL = "jdbc:mysql://localhost:3306/estatebasic";
    static final String USER = "root";
    static final String PASS = "123456";
    
    public static Connection getConnection() {
    	Connection connection = null;
    	try {
    		connection = DriverManager.getConnection(DB_URL,USER,PASS);
			
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    	return connection;
    }

}
