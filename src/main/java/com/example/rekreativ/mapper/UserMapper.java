package com.example.rekreativ.mapper;

import com.example.rekreativ.dto.ReviewRequestDto;
import com.example.rekreativ.dto.UserDTO;
import com.example.rekreativ.model.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final ReviewMapper reviewMapper;

    public UserMapper(ReviewMapper reviewMapper) {
        this.reviewMapper = reviewMapper;
    }

    public UserDTO mapToUserDto(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setUsername(user.getUsername());
        userDTO.setId(user.getId());
        userDTO.setRoles(Objects.isNull(user.getRoles()) ? Collections.emptyList() : user.getRoles());

        List<ReviewRequestDto> reviews = user.getReviews().stream()
                .map(reviewMapper::mapToReviewRequest)
                .collect(Collectors.toList());
        userDTO.setReviews(reviews);

        double rating = reviews.stream()
                .mapToDouble(ReviewRequestDto::getRating)
                .average()
                .orElse(0);
        userDTO.setAverageReviewRating(rating);


        return userDTO;
    }
}
