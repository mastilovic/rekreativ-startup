package com.example.rekreativ.service.impl;

import com.example.rekreativ.dto.request.ReviewRequestDto;
import com.example.rekreativ.error.exceptions.ObjectNotFoundException;
import com.example.rekreativ.mapper.ReviewMapper;
import com.example.rekreativ.model.Review;
import com.example.rekreativ.repository.ReviewRepository;
import com.example.rekreativ.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    public ReviewServiceImpl(ReviewRepository reviewRepository, ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
    }

    public ReviewRequestDto findReviewById(Long id) {
        return reviewRepository.findById(id)
                .map(reviewMapper::mapToReviewRequest)
                .orElseThrow(() -> new ObjectNotFoundException(Review.class, id));
    }

    public List<ReviewRequestDto> getReviews() {
        return reviewRepository.findAll().stream()
                .map(reviewMapper::mapToReviewRequest)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        log.info("calling method to delete review by id!");

        reviewRepository.findById(id)
                .ifPresentOrElse(review -> {
                            log.info("review found with id: {}", id);
                            reviewRepository.deleteById(review.getId());
                            log.info("review successfully deleted!");
                        },
                        () -> log.error("Error occured while trying to delete review!"));
    }
}
