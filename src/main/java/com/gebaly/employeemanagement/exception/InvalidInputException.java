package com.gebaly.employeemanagement.exception;


public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super(message);
    }
}