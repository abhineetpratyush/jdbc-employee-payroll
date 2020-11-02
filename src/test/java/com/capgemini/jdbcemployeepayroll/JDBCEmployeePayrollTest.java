package com.capgemini.jdbcemployeepayroll;

import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.capgemini.jdbcemployeepayroll.EmployeePayrollService.IOService;

public class JDBCEmployeePayrollTest {
	
	public EmployeePayrollService employeePayrollService;

	@Before
	public void initialise() {
		this.employeePayrollService = new EmployeePayrollService();
	}


	@Test
	public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() throws CustomJDBCException {
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		Assert.assertEquals(3, employeePayrollData.size());
	}

	@Test
	public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB() throws CustomJDBCException {
		employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		employeePayrollService.updateEmployeeSalary("Terissa", 3000000.00);
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terissa");
		Assert.assertTrue(result);
	}

	@Test
	public void givenNewSalaryForEmployee_WhenUpdatedUsingPreparedStatement_ShouldSyncWithDB() throws CustomJDBCException {
		employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		employeePayrollService.updateEmployeeSalary("Terissa", 3000000.00);
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terissa");
		Assert.assertTrue(result);
	}

}

