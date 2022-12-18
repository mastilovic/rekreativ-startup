package com.example.rekreativ.error.exceptions;

public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(Class clazz, Long id) {
        super(clazz.getSimpleName()+" not found with id "+id);
    }

    public ObjectNotFoundException(Class clazz, String name) {
        super(clazz.getSimpleName()+" not found with name "+name);
    }
}
