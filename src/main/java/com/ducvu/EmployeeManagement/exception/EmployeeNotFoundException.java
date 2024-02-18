package com.ducvu.EmployeeManagement.exception;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(String msg) {
        super(msg);
    }
}
