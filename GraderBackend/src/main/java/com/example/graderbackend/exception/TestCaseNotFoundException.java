package com.example.graderbackend.exception;

public class TestCaseNotFoundException extends RuntimeException {
    public TestCaseNotFoundException(Long id) {
        super("TestCase not found with id " + id);
    }
}