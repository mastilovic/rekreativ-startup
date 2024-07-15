package com.example.rekreativ.service;

import com.example.rekreativ.model.dto.request.ListingRequestDto;
import com.example.rekreativ.model.dto.request.PlayerTypeRequestDto;
import com.example.rekreativ.model.dto.request.UserListingUpdateRequestDto;
import com.example.rekreativ.model.dto.response.ListingResponseDto;
import com.example.rekreativ.model.Listing;
import com.example.rekreativ.model.enums.PlayerType;

import java.util.List;

public interface ListingService {
    ListingResponseDto findById(Long id);

    ListingResponseDto save(ListingRequestDto listingRequestDto);

    ListingResponseDto initSave(Listing listing);

    List<ListingResponseDto> findAll();

    void delete(Long listingId, Long userId);

    ListingResponseDto update(ListingResponseDto listingResponseDto);

    ListingResponseDto addSignedUserToListing(UserListingUpdateRequestDto userListingUpdateRequestDto);

    ListingResponseDto deleteSignedUserFromListing(UserListingUpdateRequestDto userListingUpdateRequestDto);

    ListingResponseDto addAcceptedUserToListing(UserListingUpdateRequestDto userListingUpdateRequestDto);

    ListingResponseDto deleteAcceptedUserFromListing(UserListingUpdateRequestDto userListingUpdateRequestDto);

    ListingResponseDto deactivateListing(Long listingId, Long userId);

}
