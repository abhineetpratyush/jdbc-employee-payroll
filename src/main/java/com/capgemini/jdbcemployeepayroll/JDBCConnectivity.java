package com.capgemini.jdbcemployeepayroll;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JDBCConnectivity {
	private static final Logger log = LogManager.getLogger(JDBCConnectivity.class);
	public static void main(String[] args) {
		String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service2?useSSL=false";
		String userName = "root";
		String password = "";
		Connection connection;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			log.info("Driver loaded!");
		} catch(ClassNotFoundException e) {
			throw new IllegalStateException("Cannot find the driver in the classpath:", e);
		}

		listDrivers();


		try {
			log.info("Connecting to database: " + jdbcURL);
			connection = DriverManager.getConnection(jdbcURL, userName, password);
			log.info("Connection is successful!! " + connection);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static void listDrivers() {
		Enumeration<Driver> driverList = DriverManager.getDrivers();
		while(driverList.hasMoreElements()) {
			Driver driverClass = (Driver) driverList.nextElement();
			log.info(driverClass.getClass().getName());
		}
	}
}