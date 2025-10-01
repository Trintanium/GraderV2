package com.example.graderbackend.exception;

public class TagNotFoundException extends RuntimeException {
    public TagNotFoundException(Long tagId) {
        super("Tag not found with id: " + tagId);
    }
}