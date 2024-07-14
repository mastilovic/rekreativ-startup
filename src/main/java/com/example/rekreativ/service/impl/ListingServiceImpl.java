package com.example.rekreativ.service.impl;

import com.example.rekreativ.dto.request.ListingRequestDto;
import com.example.rekreativ.dto.response.ListingResponseDto;
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
                .map(this::mapUserToListing)
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
    public ListingResponseDto initSave(Listing listing) {
        return this.mapUserToListing(listingRepository.save(listing));
    }

    @Override
    public List<ListingResponseDto> findAll() {
        return listingRepository.findAll().stream()
                .map(this::mapUserToListing)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        listingRepository.findById(id)
                .ifPresentOrElse(listing -> {
                    log.info("Listing found by id. Attempting to delete!");
                    listingRepository.deleteById(id);
                    log.info("Listing deleted successfully!!");
                }, () -> log.error("Error occured while trying to delete listing!"));
    }

    @Override
    public ListingResponseDto update(ListingResponseDto listingResponseDto) {
        return null;
    }

    @Override
    public ListingResponseDto addSignedUserToListing(Long listingId, Long userId) {
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new ObjectNotFoundException(Listing.class, listingId));
        User user = userService.findRawUserById(userId);
        listing.getSigned().add(user);
        user.getListings().add(listing);
        return this.initSave(listing);
    }

    @Override
    public ListingResponseDto deleteSignedUserFromListing(Long listingId, Long userId) {
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new ObjectNotFoundException(Listing.class, listingId));
        User user = userService.findRawUserById(userId);
        listing.getSigned().remove(user);
        user.getListings().remove(listing);

        return this.initSave(listing);
    }

    @Override
    public ListingResponseDto deleteAcceptedUserFromListing(Long listingId, Long userId) {
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new ObjectNotFoundException(Listing.class, listingId));
        User user = userService.findRawUserById(userId);
        listing.getAccepted().remove(user);
        user.getListings().remove(listing);

        return this.initSave(listing);
    }

    @Override
    public ListingResponseDto addAcceptedUserToListing(Long listingId, Long userId) {
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new ObjectNotFoundException(Listing.class, listingId));
        User user = userService.findRawUserById(userId);

        listing.getAccepted().add(user);
        user.getListings().add(listing);

        return this.initSave(listing);
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
