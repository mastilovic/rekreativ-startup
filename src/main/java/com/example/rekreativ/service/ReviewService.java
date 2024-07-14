package com.example.rekreativ.service;

import com.example.rekreativ.dto.request.ReviewRequestDto;

import java.util.List;

public interface ReviewService {
    ReviewRequestDto findReviewById(Long id);

    List<ReviewRequestDto> getReviews();

    void delete(Long id);
}
