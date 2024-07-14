package com.example.rekreativ.service;

import com.example.rekreativ.dto.request.ListingRequestDto;
import com.example.rekreativ.dto.response.ListingResponseDto;
import com.example.rekreativ.model.Listing;

import java.util.List;

public interface ListingService {
    ListingResponseDto findById(Long id);

    ListingResponseDto save(ListingRequestDto listingRequestDto);

    ListingResponseDto initSave(Listing listing);

    List<ListingResponseDto> findAll();

    void delete(Long id);

    ListingResponseDto update(ListingResponseDto listingResponseDto);

    ListingResponseDto addSignedUserToListing(Long listingId, Long userId);

    ListingResponseDto deleteSignedUserFromListing(Long listingId, Long userId);

    ListingResponseDto addAcceptedUserToListing(Long listingId, Long userId);

    ListingResponseDto deleteAcceptedUserFromListing(Long listingId, Long userId);

}
