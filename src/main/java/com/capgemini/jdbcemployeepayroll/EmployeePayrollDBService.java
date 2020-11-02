package com.capgemini.jdbcemployeepayroll;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmployeePayrollDBService {
	private static final Logger log = LogManager.getLogger(EmployeePayrollDBService.class);

	private Connection getConnection() throws CustomJDBCException {
		String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service2?useSSL=false";
		String userName = "root";
		String password = "";
		Connection connection;
		log.info("Connecting to database: " + jdbcURL);
		try {
			connection = DriverManager.getConnection(jdbcURL, userName, password);
			log.info("Connection is successful!! " + connection);
			return connection;
		} catch (SQLException e) {
			throw new CustomJDBCException(ExceptionType.SQL_EXCEPTION);
		}
	}

	public List<EmployeePayrollData> readData() throws CustomJDBCException {
		String sql = "select * from employee_payroll";
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		try (Connection connection = this.getConnection()){
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while(resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				double salary = resultSet.getDouble("salary");
				LocalDate startDate = resultSet.getDate("start").toLocalDate();
				employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate));
			}
		} catch (SQLException e) {
			throw new CustomJDBCException(ExceptionType.SQL_EXCEPTION);
		}
		return employeePayrollList;
	}

	public int updateEmployeeData(String name, double salary) throws CustomJDBCException {
		return this.updateEmployeeDetailsUsingStatement(name, salary);
	}
	
	public List<EmployeePayrollData> getEmployeePayrollDataFromDB(String name) throws CustomJDBCException  {
		String sql = String.format("select * from employee_payroll where name = '%s'", name);
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		try (Connection connection = this.getConnection()){
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while(resultSet.next()) {
				int id = resultSet.getInt("id");
				String emp_name = resultSet.getString("name");
				double salary = resultSet.getDouble("salary");
				LocalDate start = resultSet.getDate("start").toLocalDate();
				employeePayrollList.add(new EmployeePayrollData(id, emp_name, salary, start));
			}
			return employeePayrollList;
		} catch (SQLException e) {
			throw new CustomJDBCException(ExceptionType.SQL_EXCEPTION);
		}
	}

	private int updateEmployeeDetailsUsingStatement(String name, double salary) throws CustomJDBCException {
		String sql = String.format("update employee_payroll set salary = %.2f where name = '%s';", salary, name);
		try (Connection connection = this.getConnection()){
			Statement statement = connection.createStatement();
			return statement.executeUpdate(sql);
		} catch (SQLException e) {
			throw new CustomJDBCException(ExceptionType.SQL_EXCEPTION);
		}
	}
}