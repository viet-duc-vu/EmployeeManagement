package com.ducvu.EmployeeManagement.service;

import com.ducvu.EmployeeManagement.exception.EmployeeAlreadyExistException;
import com.ducvu.EmployeeManagement.exception.EmployeeNotFoundException;
import com.ducvu.EmployeeManagement.model.Employee;
import com.ducvu.EmployeeManagement.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeService underTest;

    @Captor
    private ArgumentCaptor<Employee> employeeArgumentCaptor;

    @Test
    void shouldGetAllEmployees() {
        underTest.getAllEmployees();

        verify(employeeRepository).findAll();
    }

    @Test
    void shouldAddEmployee() {
        Employee employee = new Employee("First", "Last", "firstlast@gmail.com", "testDP");

        underTest.addEmployee(employee);

        verify(employeeRepository).save(employeeArgumentCaptor.capture());
        Employee value = employeeArgumentCaptor.getValue();
        assertThat(value.getFirstName()).isEqualTo("First");
        assertThat(value.getLastName()).isEqualTo("Last");
        assertThat(value.getEmail()).isEqualTo("firstlast@gmail.com");
        assertThat(value.getDepartment()).isEqualTo("testDP");
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExisted() {
        Employee employee = new Employee("First", "Last", "firstlast@gmail.com", "testDP");

        given(employeeRepository.findEmployeeByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));

        assertThatThrownBy(() -> underTest.addEmployee(employee))
                .isInstanceOf(EmployeeAlreadyExistException.class)
                .hasMessageContaining(employee.getEmail() + " already exist!");

        verify(employeeRepository, never()).save(any());
    }

    @Test
    void shouldDeleteEmployeeIfExisted() {
        long id = 1;

        given(employeeRepository.existsById(id))
                .willReturn(true);

        underTest.deleteEmployee(id);

        verify(employeeRepository).deleteById(id);
    }

    @Test
    void shouldThrowExceptionWhenEmployeeDoesNotExist() {
        long employeeID = 1;

        given(employeeRepository.existsById(employeeID))
                .willReturn(false);

        assertThatThrownBy(() -> underTest.deleteEmployee(employeeID))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessageContaining("No employee found with id: " + employeeID);

        verify(employeeRepository, never()).deleteById(any());
    }

    @Test
    void shouldGetEmployeeById() {
        long employeeID = 99L;
        Employee employee = new Employee(employeeID,"First", "Last", "firstlast@gmail.com", "testDP");

        given(employeeRepository.findById(employeeID))
                .willReturn(Optional.of(employee));

        Employee returnedEmployee = underTest.getEmployeeById(employeeID);
        verify(employeeRepository).findById(employeeID);

        assertThat(returnedEmployee.getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(returnedEmployee.getLastName()).isEqualTo(employee.getLastName());
        assertThat(returnedEmployee.getEmail()).isEqualTo(employee.getEmail());
        assertThat(returnedEmployee.getDepartment()).isEqualTo(employee.getDepartment());

    }

    @Test
    void shouldThrowExceptionWhenGettingNonExistedEmployee() {
        long employeeID = 99L;

        given(employeeRepository.findById(employeeID))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getEmployeeById(employeeID))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessageContaining("No employee found with id: " + employeeID);

    }

    @Test
    void shouldUpdateEmployee() {
        long employeeID = 99L;
        Employee existingEmployee = new Employee(employeeID, "First", "Last", "firstlast@gmail.com", "testDP");
        Employee updatedEmployee = new Employee("UFirst", "ULast", "Ufirstlast@gmail.com", "testDP");

        given(employeeRepository.findEmployeeByEmail(updatedEmployee.getEmail())).willReturn(Optional.empty());
        given(employeeRepository.findById(employeeID)).willReturn(Optional.of(existingEmployee));
        given(employeeRepository.save(any(Employee.class))).willReturn(updatedEmployee);


        Employee result = assertDoesNotThrow(() -> underTest.updateEmployee(updatedEmployee, employeeID));


        assertThat(result.getFirstName()).isEqualTo(updatedEmployee.getFirstName());
        assertThat(result.getLastName()).isEqualTo(updatedEmployee.getLastName());
        assertThat(result.getEmail()).isEqualTo(updatedEmployee.getEmail());
        assertThat(result.getDepartment()).isEqualTo(updatedEmployee.getDepartment());


        verify(employeeRepository, times(1)).findById(employeeID);
        verify(employeeRepository, times(1)).save(any(Employee.class));

    }

    @Test
    void shouldThrowEmployeeAlreadyExistException() {
        Employee existingEmployee = new Employee(1L, "First", "Last", "existing@gmail.com", "testDP");
        Employee newEmployee = new Employee("UFirst", "ULast", "existing@gmail.com", "testDP");

        given(employeeRepository.findEmployeeByEmail(newEmployee.getEmail())).willReturn(Optional.of(existingEmployee));

        assertThatThrownBy(() -> underTest.updateEmployee(newEmployee, 1l))
                .isInstanceOf(EmployeeAlreadyExistException.class);

        verify(employeeRepository, never()).findById(anyLong());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void shouldThrowEmployeeNotFoundException() {
        Employee newEmployee = new Employee("UFirst", "ULast", "existing@gmail.com", "testDP");

        given(employeeRepository.findById(anyLong())).willReturn(Optional.empty());


        assertThatThrownBy(() -> underTest.updateEmployee(newEmployee, 1L))
                .isInstanceOf(EmployeeNotFoundException.class);


        verify(employeeRepository, times(1)).findById(anyLong());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

}
