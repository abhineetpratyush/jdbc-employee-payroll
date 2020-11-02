package com.capgemini.jdbcemployeepayroll;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
	private static EmployeePayrollDBService employeePayrollDBService;
	private PreparedStatement preparedStatement;
	private PreparedStatement preparedStatemetForRetrieval;
	private EmployeePayrollDBService() {}

	public static EmployeePayrollDBService getInstance() {
		if(employeePayrollDBService == null) {
			employeePayrollDBService = new EmployeePayrollDBService();
		}
		return employeePayrollDBService;
	}
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
		try (Connection connection = this.getConnection()){
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			return this.getEmployeePayrollListFromResultSet(resultSet);
		} catch (SQLException e) {
			throw new CustomJDBCException(ExceptionType.SQL_EXCEPTION);
		}
	}

	private List<EmployeePayrollData> getEmployeePayrollListFromResultSet(ResultSet resultSet) throws CustomJDBCException {
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		try {
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String employee_name = resultSet.getString("name");
				double salary = resultSet.getDouble("salary");
				LocalDate start = resultSet.getDate("start").toLocalDate();
				employeePayrollList.add(new EmployeePayrollData(id, employee_name, salary, start));
			}
			return employeePayrollList;
		} catch (SQLException e) {
			throw new CustomJDBCException(ExceptionType.RESULT_SET_PROBLEM);
		}
	}

	public int updateEmployeeData(String name, double salary) throws CustomJDBCException {
		return this.updateEmployeePayrollDataUsingPreparedStatement(name, salary);
	}

	private int updateEmployeePayrollDataUsingPreparedStatement(String name, double salary) throws CustomJDBCException {
		if(this.preparedStatement==null) {
			this.prepareStatementForEmployeePayroll();
		}
		try {
			preparedStatement.setDouble(1, salary);
			preparedStatement.setString(2, name);
			int rowsAffected=preparedStatement.executeUpdate();
			return rowsAffected;
		}catch(SQLException e) {
			throw new CustomJDBCException(ExceptionType.UNABLE_TO_USE_PREPARED_STATEMENT);
		}
	}

	private void prepareStatementForEmployeePayroll() throws CustomJDBCException {
		try {
			Connection connection = this.getConnection();
			String sql = "update employee_payroll set salary = ? where name = ?";
			this.preparedStatement = connection.prepareStatement(sql);
		}catch (SQLException e) {
			throw new CustomJDBCException(ExceptionType.UNABLE_TO_USE_PREPARED_STATEMENT);
		}

	}

	public List<EmployeePayrollData> getEmployeePayrollDataFromDB(String name) throws CustomJDBCException  {
		if (this.preparedStatemetForRetrieval == null) {
			this.prepareStatementForEmployeePayrollDataRetrieval();
		}
		try (Connection connection = this.getConnection()) {
			this.preparedStatemetForRetrieval.setString(1, name);
			ResultSet resultSet = preparedStatemetForRetrieval.executeQuery();
			return this.getEmployeePayrollListFromResultSet(resultSet);
		} catch (SQLException e) {
			throw new CustomJDBCException(ExceptionType.SQL_EXCEPTION);
		}
	}

	private void prepareStatementForEmployeePayrollDataRetrieval() throws CustomJDBCException {
		try {
			Connection connection = this.getConnection();
			String sql = "select * from employee_payroll where name = ?";
			this.preparedStatemetForRetrieval = connection.prepareStatement(sql);
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
			throw new CustomJDBCException(ExceptionType.UNABLE_TO_USE_STATEMENT);
		}
	}

	public List<EmployeePayrollData> getEmployeePayrollDataInDateRange(LocalDate startDate, LocalDate endDate) throws CustomJDBCException {
		String sql = String.format("select * from employee_payroll where start between cast('%s' as date) and cast('%s' as date);",
				startDate.toString(), endDate.toString());
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			return this.getEmployeePayrollListFromResultSet(resultSet);
		} catch (SQLException e) {
			throw new CustomJDBCException(ExceptionType.SQL_EXCEPTION);
		}
	}
}
