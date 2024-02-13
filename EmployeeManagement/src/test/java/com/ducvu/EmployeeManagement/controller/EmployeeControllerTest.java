package com.ducvu.EmployeeManagement.controller;

import com.ducvu.EmployeeManagement.model.Employee;
import com.ducvu.EmployeeManagement.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmployeeController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetAllEmployees() throws Exception{
        List<Employee> employees = Arrays.asList(
                new Employee("First1", "Last1", "test1@gmail.com", "test"),
                new Employee("First2", "Last2", "test2@gmail.com", "test")
        );

        given(employeeService.getAllEmployees()).willReturn(employees);

        mockMvc.perform(get("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(employees.size())));
    }

    @Test
    void shouldGetEmployeeById() throws Exception {
        long employeeId = 1;

        Employee employee = new Employee("First", "Last", "test@gmail.com", "test");
        given(employeeService.getEmployeeById(employeeId)).willReturn(employee);

        mockMvc.perform(get("/api/v1/employees/employee/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    @Test
    void shouldAddNewEmployee() throws Exception {

        Employee employee = new Employee("First", "Last", "test@gmail.com", "test");
        given(employeeService.addEmployee(ArgumentMatchers.any())).willAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(employee.getEmail())))
                .andDo(print());

    }

    @Test
    void shouldUpdateEmployee() throws Exception{
        long employeeId = 1;
        Employee employee = new Employee("First", "Last", "test@gmail.com", "test");
        given(employeeService.updateEmployee(employee, employeeId)).willReturn(employee);

        mockMvc.perform(put("/api/v1/employees/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(employee.getEmail())))
                .andDo(print());
    }

    @Test
    void shouldDeleteEmployee() throws Exception {
        long employeeId = 1;
        doNothing().when(employeeService).deleteEmployee(employeeId);

        mockMvc.perform(delete("/api/v1/employees/delete/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }


}
