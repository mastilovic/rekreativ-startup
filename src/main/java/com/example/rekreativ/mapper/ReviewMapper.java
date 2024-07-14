package com.example.rekreativ.mapper;

import com.example.rekreativ.dto.request.ReviewRequestDto;
import com.example.rekreativ.model.Review;
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
}
