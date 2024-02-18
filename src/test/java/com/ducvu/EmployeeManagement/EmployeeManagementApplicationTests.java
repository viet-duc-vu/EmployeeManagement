package com.ducvu.EmployeeManagement;

import com.ducvu.EmployeeManagement.controller.EmployeeController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EmployeeManagementApplicationTests {

	@Autowired
	private EmployeeController employeeController;

	@Test
	void contextLoads() {

	}

}
