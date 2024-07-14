package com.example.rekreativ.mapper;

import com.example.rekreativ.dto.request.ListingRequestDto;
import com.example.rekreativ.dto.response.ListingResponseDto;
import com.example.rekreativ.model.Listing;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class ListingMapper {

    public ListingResponseDto mapToResponseDto(Listing listing) {
        ListingResponseDto response = new ListingResponseDto();

        response.setListingType(listing.getListingType());
        response.setLookingFor(new HashSet<>(listing.getLookingFor()));
        response.setDescription(listing.getDescription());
        response.setNeedPlayersCount(listing.getNeedPlayersCount());
        response.setCreatedAt(listing.getCreatedAt());
//        response.setCreatedBy(userMapper.mapToUserListingDto(listing.getCreatedBy()));
//        response.setSigned(listing.getSigned().stream()
//                                   .map(userMapper::mapToUserListingDto)
//                                   .collect(Collectors.toList()));
//        response.setAccepted(listing.getAccepted().stream()
//                                     .map(userMapper::mapToUserListingDto)
//                                     .collect(Collectors.toList()));
        response.setMatchDate(listing.getMatchDate());
        response.setTitle(listing.getTitle());
        response.setActive(listing.getActive());

        return response;
    }

    public Listing mapToListing(ListingRequestDto listingRequestDto) {
        Listing listing = new Listing();
//        EnumSet<PlayerType> playerTypes = EnumSet.noneOf(PlayerType.class);
//        playerTypes.addAll(listingRequestDto.getLookingFor());

        listing.setListingType(listingRequestDto.getListingType());
        listing.setLookingFor(listingRequestDto.getLookingFor());
        listing.setTitle(listingRequestDto.getTitle());
        listing.setDescription(listingRequestDto.getDescription());
        listing.setMatchDate(listingRequestDto.getMatchDate());
        listing.setNeedPlayersCount(listingRequestDto.getNeedPlayersCount());

        return listing;
    }
}
