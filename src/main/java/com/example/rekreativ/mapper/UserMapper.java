package com.example.rekreativ.mapper;

import com.example.rekreativ.dto.UserListingDto;
import com.example.rekreativ.dto.request.ReviewRequestDto;
import com.example.rekreativ.dto.response.UserResponseDTO;
import com.example.rekreativ.model.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final ReviewMapper reviewMapper;
    private final ListingMapper listingMapper;

    public UserMapper(ReviewMapper reviewMapper, ListingMapper listingMapper) {
        this.reviewMapper = reviewMapper;
        this.listingMapper = listingMapper;
    }

    public UserListingDto mapToUserListingDto(User user) {
        UserListingDto userListingDto = new UserListingDto();
        List<ReviewRequestDto> reviews = Objects.isNull(user.getReviews()) || user.getReviews().isEmpty()
                                         ? Collections.emptyList()
                                         : user.getReviews().stream()
                                                 .map(reviewMapper::mapToReviewRequest)
                                                 .collect(Collectors.toList());
        userListingDto.setUsername(user.getUsername());
        userListingDto.setId(user.getId());
        userListingDto.setRoles(Objects.isNull(user.getRoles()) || user.getRoles().isEmpty()
                                ? Collections.emptyList()
                                : user.getRoles());
        userListingDto.setReviews(reviews);
        userListingDto.setAverageReviewRating(reviews.isEmpty()
                                              ? 0.0
                                              : reviews.stream()
                                                      .filter(review -> Objects.nonNull(review.getRating()))
                                                      .mapToDouble(ReviewRequestDto::getRating)
                                                      .average()
                                                      .orElse(0.0));
        userListingDto.setReviewsCount(reviews.size());
        return userListingDto;
    }

    public UserResponseDTO mapToUserResponseDto(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();

        List<ReviewRequestDto> reviews = Objects.isNull(user.getReviews()) || user.getReviews().isEmpty()
                                         ? Collections.emptyList()
                                         : user.getReviews().stream()
                                                 .map(reviewMapper::mapToReviewRequest)
                                                 .collect(Collectors.toList());
        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setId(user.getId());
        userResponseDTO.setRoles(Objects.isNull(user.getRoles()) || user.getRoles().isEmpty()
                                 ? Collections.emptyList()
                                 : user.getRoles());
        userResponseDTO.setReviews(reviews);
        userResponseDTO.setAverageReviewRating(reviews.size() < 1
                                               ? 0.0
                                               : reviews.stream()
                                                       .filter(review -> Objects.nonNull(review.getRating()))
                                                       .mapToDouble(ReviewRequestDto::getRating)
                                                       .average()
                                                       .orElse(0.0));
        userResponseDTO.setReviewsCount(reviews.size());
        userResponseDTO.setListings(user.getListings().stream()
                                            .map(listingMapper::mapToResponseDto)
                                            .collect(Collectors.toList()));
        return userResponseDTO;
    }
}
