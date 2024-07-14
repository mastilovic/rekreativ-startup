package com.example.rekreativ.dto.request;

import com.example.rekreativ.model.Role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserRequestDto {
    private String username;
    private Collection<Role> roles = new ArrayList<>();
    private double averageReviewRating;
    private List<ReviewRequestDto> reviews = new ArrayList<>();
}
