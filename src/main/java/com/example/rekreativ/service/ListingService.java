package com.example.rekreativ.service;

import com.example.rekreativ.dto.request.ListingRequestDto;
import com.example.rekreativ.dto.response.ListingResponseDto;

import java.util.List;

public interface ListingService {
    ListingResponseDto findById(Long id);

    ListingResponseDto save(ListingRequestDto listingRequestDto);

    ListingResponseDto initSave(ListingResponseDto listingResponseDto);

    List<ListingResponseDto> findAll();

    void delete(Long id);

    ListingResponseDto update(ListingResponseDto listingResponseDto);

    ListingResponseDto addUserToListing(Long listingId, Long userId);

    ListingResponseDto deleteUserFromListing(Long listingId, Long userId);

}
