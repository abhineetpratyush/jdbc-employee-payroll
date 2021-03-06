package com.capgemini.jdbcemployeepayroll;

import java.time.LocalDate;
import java.util.List;

public class EmployeePayrollService {

	public enum IOService {
		CONSOLE_IO, FILE_IO, DB_IO, REST_IO
	}
	private EmployeePayrollDBService employeePayrollDBService;
	private List<EmployeePayrollData> employeePayrollList;
	public EmployeePayrollService() {
		this.employeePayrollDBService = EmployeePayrollDBService.getInstance();
	}

	public List<EmployeePayrollData> readEmployeePayrollData(IOService ioService) throws CustomJDBCException {
		if(ioService.equals(IOService.DB_IO))
			this.employeePayrollList = employeePayrollDBService.readData();
		return this.employeePayrollList;
	}

	public void updateEmployeeSalary(String name, double salary) throws CustomJDBCException {
		int result = employeePayrollDBService.updateEmployeeData(name, salary);
		if (result == 0) 
			throw new CustomJDBCException(ExceptionType.RECORD_UPDATE_FAILURE);
		EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
		if(employeePayrollData != null) 
			employeePayrollData.salary = salary;
	}

	private EmployeePayrollData getEmployeePayrollData(String name) {
		return this.employeePayrollList.stream()
				.filter(employeePayrollDataItem -> employeePayrollDataItem.name.equals(name))
				.findFirst()
				.orElse(null);
	}

	public boolean checkEmployeePayrollInSyncWithDB(String name) throws CustomJDBCException {
		List<EmployeePayrollData> employeePayrollDataList = employeePayrollDBService.getEmployeePayrollDataFromDB(name);
		return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
	}

	public List<EmployeePayrollData> getEmployeePayrollDataInDateRange(LocalDate startDate, LocalDate endDate) throws CustomJDBCException {
		return this.employeePayrollDBService.getEmployeePayrollDataInDateRange(startDate, endDate);
	}

	public QueryResultStructure performSQLFunction(SQLFunctionType functionType) throws CustomJDBCException {
		return this.employeePayrollDBService.performSQLFunction(functionType);
	}

	public void addEmployeeToPayroll(String name, double salary, LocalDate startDate, String gender, int companyId, String companyName, int departmentId) throws CustomJDBCException {
		employeePayrollList.add(employeePayrollDBService.addEmployeeToPayroll(name, salary, startDate, gender, companyId, companyName, departmentId));
	}

	public void deleteEmployeeFromPayroll(String name) throws CustomJDBCException {
		employeePayrollList.remove(employeePayrollDBService.deleteEmployeeFromPayroll(name));
	}

	public boolean checkEmployeeDeleted(String employeeName) throws CustomJDBCException {
		boolean listCheck = employeePayrollList.stream().anyMatch(employee -> employee.name.equals(employeeName));
		boolean databaseCheck = employeePayrollDBService.checkIfEmployeeActive(employeeName);
		if(listCheck == false && databaseCheck == false)
			return false;
		else 
			return true;
	}
}