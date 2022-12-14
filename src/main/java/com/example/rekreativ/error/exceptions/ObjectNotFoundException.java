package com.example.rekreativ.error.exceptions;

public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(Class clazz, Long id) {
        super(clazz.getSimpleName()+" already exists with id "+id);
    }

    public ObjectNotFoundException(Class clazz, String name) {
        super(clazz.getSimpleName()+" already exists with name "+name);
    }
}
