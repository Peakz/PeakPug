package com.github.peakz.DAO;

import com.mysql.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	public final static String URL = "";
	public final static String USER = "";
	public final static String PASS = "";

	/**
	 * Get a connection to database
	 * @return Connection object
	 */
	public static Connection getConnection()
	{
		try {
			DriverManager.registerDriver(new Driver());
			return DriverManager.getConnection(URL, USER, PASS);
		} catch (SQLException ex) {
			throw new RuntimeException("Error connecting to the database", ex);
		}
	}
}
