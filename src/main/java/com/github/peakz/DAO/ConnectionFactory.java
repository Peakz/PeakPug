package com.github.peakz.DAO;

import com.mysql.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	public final static String URL = "jdbc:mysql://45.76.43.30/pug_db?useSSL=false";
	public final static String USER = "peak";
	public final static String PASS = "XU84bXwGlc5odiEn";

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
