package com.example.RekreativStartup.util;

import com.example.RekreativStartup.model.Team;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ValidatorUtil {

    public static boolean teamValidator(Team team){
        return !StringUtils.isBlank(String.valueOf(team));
    }
}
