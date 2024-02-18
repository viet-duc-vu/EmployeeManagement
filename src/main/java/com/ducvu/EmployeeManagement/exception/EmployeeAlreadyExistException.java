package com.ducvu.EmployeeManagement.exception;

public class EmployeeAlreadyExistException extends RuntimeException {
    public EmployeeAlreadyExistException(String message) {
        super(message);
    }
}
