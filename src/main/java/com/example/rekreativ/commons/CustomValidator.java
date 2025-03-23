package com.example.rekreativ.commons;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

@Service
@Slf4j
//TODO: util class only has static methods, refactor name of the class
public class CustomValidator {

    private final Validator validator;

    public CustomValidator(Validator validator) {
        this.validator = validator;
    }

    public <T> void validate(T t) {
        log.debug("calling validate method");

        Set<ConstraintViolation<T>> violations = validator.validate(t);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
