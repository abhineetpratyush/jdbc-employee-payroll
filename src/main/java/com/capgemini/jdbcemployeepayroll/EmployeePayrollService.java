package com.capgemini.jdbcemployeepayroll;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmployeePayrollService {
	public enum IOService {
		CONSOLE_IO, FILE_IO, DB_IO, REST_IO
	}
	private static final Logger log = LogManager.getLogger(EmployeePayrollService.class);
	private EmployeePayrollDBService employeePayrollDBService;
	private List<EmployeePayrollData> employeePayrollList;
	public EmployeePayrollService() {
		this.employeePayrollDBService = new EmployeePayrollDBService();
	}
	public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList) {
		this.employeePayrollList = employeePayrollList;
	}
	
	public List<EmployeePayrollData> readEmployeePayrollData(IOService ioService) throws CustomJDBCException {
		if(ioService.equals(IOService.DB_IO))
			this.employeePayrollList = new EmployeePayrollDBService().readData();
		return this.employeePayrollList;
	}
	
	public void updateEmployeeSalary(String name, double salary) throws CustomJDBCException {
		int result = new EmployeePayrollDBService().updateEmployeeData(name, salary);
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
		List<EmployeePayrollData> employeePayrollDataList = new EmployeePayrollDBService().getEmployeePayrollDataFromDB(name);
		return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
	}
}