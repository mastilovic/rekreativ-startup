package com.example.rekreativ.service.impl;

import com.example.rekreativ.dto.request.ListingRequestDto;
import com.example.rekreativ.dto.response.ListingResponseDto;
import com.example.rekreativ.dto.response.UserResponseDTO;
import com.example.rekreativ.error.exceptions.ObjectNotFoundException;
import com.example.rekreativ.mapper.ListingMapper;
import com.example.rekreativ.mapper.UserMapper;
import com.example.rekreativ.model.Listing;
import com.example.rekreativ.model.User;
import com.example.rekreativ.repository.ListingRepository;
import com.example.rekreativ.service.ListingService;
import com.example.rekreativ.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ListingServiceImpl implements ListingService {

    private final ListingRepository listingRepository;
    private final ListingMapper listingMapper;
    private final UserMapper userMapper;
    private final UserService userService;

    public ListingServiceImpl(ListingRepository listingRepository,
                              ListingMapper listingMapper,
                              UserMapper userMapper,
                              UserService userService) {
        this.listingRepository = listingRepository;
        this.listingMapper = listingMapper;
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @Override
    public ListingResponseDto findById(Long id) {
        return listingRepository.findById(id)
                .map(listingMapper::mapToResponseDto)
                .orElseThrow(() -> new ObjectNotFoundException(Listing.class, id));
    }

    @Override
    @Transactional
    public ListingResponseDto save(ListingRequestDto listingRequestDto) {
        Listing listing = listingMapper.mapToListing(listingRequestDto);
        User user = userService.findRawUserById(listingRequestDto.getUserId());
        listing.setCreatedAt(Instant.now());
        listing.setCreatedBy(user);
        listing.setActive(true);

        user.getListings().add(listing);

        return mapUserToListing(listing);
    }

    @Override
    public ListingResponseDto initSave(ListingResponseDto listingResponseDto) {
        return null;
    }

    @Override
    public List<ListingResponseDto> findAll() {
        return List.of();
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public ListingResponseDto update(ListingResponseDto listingResponseDto) {
        return null;
    }

    @Override
    public ListingResponseDto addUserToListing(Long listingId, Long userId) {
        return null;
    }

    @Override
    public ListingResponseDto deleteUserFromListing(Long listingId, Long userId) {
        return null;
    }

    private ListingResponseDto mapUserToListing(Listing listing) {
        ListingResponseDto listingResponseDto = listingMapper.mapToResponseDto(listingRepository.save(listing));
        listingResponseDto.setCreatedBy(userMapper.mapToUserListingDto(listing.getCreatedBy()));
        listingResponseDto.setSigned(listing.getSigned().stream()
                                             .map(userMapper::mapToUserListingDto)
                                             .collect(Collectors.toList()));
        listingResponseDto.setAccepted(listing.getAccepted().stream()
                                               .map(userMapper::mapToUserListingDto)
                                               .collect(Collectors.toList()));
        return listingResponseDto;
    }
}
