package com.ducvu.EmployeeManagement.service;

import com.ducvu.EmployeeManagement.exception.EmployeeAlreadyExistException;
import com.ducvu.EmployeeManagement.exception.EmployeeNotFoundException;
import com.ducvu.EmployeeManagement.model.Employee;
import com.ducvu.EmployeeManagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("No employee found with id: " + id));
    }

    public Employee addEmployee(Employee e) {
        if (employeeAlreadyExist(e.getEmail())) {
            throw new EmployeeAlreadyExistException(e.getEmail() + " already exist!");
        }

        return employeeRepository.save(e);
    }

    public Employee updateEmployee(Employee e, Long id) {
        return employeeRepository.findById(id).map(employee -> {
            employee.setFirstName(e.getFirstName());
            employee.setLastName(e.getLastName());
            employee.setEmail(e.getEmail());
            employee.setDepartment(e.getDepartment());
            return employeeRepository.save(employee);
        }).orElseThrow(() -> new EmployeeNotFoundException("No employee found!"));
    }

    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id))
            throw new EmployeeNotFoundException("No employee found with id: " + id);
        employeeRepository.deleteById(id);
    }

    private boolean employeeAlreadyExist(String email) {
        return employeeRepository.findEmployeeByEmail(email).isPresent();
    }
}
