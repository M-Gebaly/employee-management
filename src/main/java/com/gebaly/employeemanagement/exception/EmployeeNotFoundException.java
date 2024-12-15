package com.gebaly.employeemanagement.exception;

import java.util.UUID;


public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(UUID id) {
        super("Employee id not found : " + id);
    }
}
