package com.example.rekreativ.mapper;

import com.example.rekreativ.model.dto.request.ReviewRequestDto;
import com.example.rekreativ.model.Review;
import com.example.rekreativ.model.dto.response.ReviewResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public Review mapToReview(ReviewRequestDto reviewRequestDto) {
        Review review = new Review();

        review.setTitle(reviewRequestDto.getTitle());
        review.setDescription(reviewRequestDto.getDescription());
        review.setRating(reviewRequestDto.getRating());

        return review;
    }

    public ReviewRequestDto mapToReviewRequest(Review review) {
        ReviewRequestDto requestDto = new ReviewRequestDto();
        requestDto.setDescription(review.getDescription());
        requestDto.setRating(review.getRating());
        requestDto.setTitle(review.getTitle());

        return requestDto;
    }

    public ReviewResponseDto mapToReviewResponseDto(Review review){
        ReviewResponseDto reviewResponseDto = new ReviewResponseDto();
        reviewResponseDto.setId(review.getId());
        reviewResponseDto.setReviewDate(review.getReviewDate());
        reviewResponseDto.setPlayerId(review.getPlayer().getId());
        reviewResponseDto.setRating(review.getRating());
        reviewResponseDto.setTitle(review.getTitle());
        reviewResponseDto.setDescription(review.getDescription());

        return reviewResponseDto;
    }
}
