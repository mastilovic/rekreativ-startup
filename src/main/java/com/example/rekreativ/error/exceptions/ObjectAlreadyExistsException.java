package com.example.rekreativ.error.exceptions;

public class ObjectAlreadyExistsException extends RuntimeException {

    public ObjectAlreadyExistsException(String message) {
        super(message);
    }

    public ObjectAlreadyExistsException(Class clazz, Long id) {
        super(clazz.getSimpleName() + " already exists with id " + id);
    }

    public ObjectAlreadyExistsException(Class clazz, String name) {
        super(clazz.getSimpleName() + " already exists with name " + name);
    }
}
