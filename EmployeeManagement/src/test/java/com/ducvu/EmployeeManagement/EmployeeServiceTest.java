package com.ducvu.EmployeeManagement;

import com.ducvu.EmployeeManagement.exception.EmployeeNotFoundException;
import com.ducvu.EmployeeManagement.model.Employee;
import com.ducvu.EmployeeManagement.repository.EmployeeRepository;
import com.ducvu.EmployeeManagement.service.EmployeeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class EmployeeServiceTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void setupDatabase() {
        Employee employee = new Employee(1l, "TestF", "TestL", "testFL@gmail.com", "Customer Service");
        employeeRepository.save(employee);
    }

    @AfterEach
    public void cleanUp() {
        jdbc.execute("DELETE FROM employee");
        jdbc.execute("ALTER TABLE employee ALTER COLUMN ID RESTART WITH 1");
    }

    @Test
    public void shouldCreateEmployee() {

        employeeService.addEmployee(new Employee(0l, "TestFirst", "TestLast", "test@gmail.com", "Test"));

        Employee e = employeeRepository.findEmployeeByEmail("test@gmail.com").orElseThrow(() -> new EmployeeNotFoundException("Not found!"));

        assertEquals("test@gmail.com", e.getEmail());

    }

    @Test
    public void shouldDeleteEmployee() {

        assertTrue(employeeRepository.existsById(1l));

        employeeService.deleteEmployee(1l);

        assertFalse(employeeRepository.existsById(1l));
    }

    @Sql("/insertData.sql")
    @Test
    public void shouldGetAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        assertEquals(5, employees.size());
    }
}
