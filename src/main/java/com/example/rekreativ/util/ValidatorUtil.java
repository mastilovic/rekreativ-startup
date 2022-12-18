package com.example.rekreativ.util;

import com.example.rekreativ.model.Role;
import com.example.rekreativ.model.Team;
import com.example.rekreativ.model.Teammate;
import com.example.rekreativ.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.Set;

@Service
public class ValidatorUtil {
    //todo: rework validator, maybe use javax validator with annotations


    private final Validator validator;

    public ValidatorUtil(Validator validator) {
        this.validator = validator;
    }


    public <T> void validate(T t){
        Set<ConstraintViolation<T>> violations = validator.validate(t);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }



//    public void userValidator(User user) {
//         if(StringUtils.isBlank(user.getUsername()) ||
//                StringUtils.isBlank(user.getPassword()) ||
//                StringUtils.containsWhitespace(user.getPassword())) {
//
//             throw new ValidationException("Validation Failed");
//         }
//    }
//
//    public void teammateValidator(Teammate teammate) {
//
//        if (StringUtils.isBlank(teammate.getName())) {
//
//            throw new ValidationException("Validation failed");
//        }
//    }
//
//    public void teamValidator(Team team) {
//
//        if (StringUtils.isBlank(team.getTeamName())) {
//
//            throw new ValidationException("Validation Failed");
//        }
//    }
//
//    public void roleValidator(Role role) {
//
//        if (StringUtils.isBlank(role.getName())) {
//
//            throw new ValidationException("Validation Failed");
//        }
//    }
}
