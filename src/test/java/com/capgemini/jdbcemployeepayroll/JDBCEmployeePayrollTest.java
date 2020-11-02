package com.capgemini.jdbcemployeepayroll;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.capgemini.jdbcemployeepayroll.EmployeePayrollService.IOService;

public class JDBCEmployeePayrollTest {
	
	@Test
	public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		Assert.assertEquals(3, employeePayrollData.size());
	}
}
