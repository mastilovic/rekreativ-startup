package com.example.rekreativ.service.impl;

import com.example.rekreativ.model.dto.UserListingDto;
import com.example.rekreativ.model.dto.request.ListingRequestDto;
import com.example.rekreativ.model.dto.request.UserListingUpdateRequestDto;
import com.example.rekreativ.model.dto.response.ListingResponseDto;
import com.example.rekreativ.error.exceptions.IllegalParameterException;
import com.example.rekreativ.error.exceptions.ObjectAlreadyExistsException;
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
import java.util.Objects;
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

        if(Boolean.TRUE.equals(user.getActiveListing()))
            throw new IllegalParameterException("This user already has active listing! Close previous listing in order to create new one!");

        listing.setCreatedAt(Instant.now());
        listing.setCreatedBy(user);
        listing.setActive(true);
        user.getListings().add(listing);
        user.setActiveListing(true);

        return this.initSave(listing);
    }

    @Override
    @Transactional
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
    @Transactional
    public void delete(Long listingId, Long userId) {
        listingRepository.findById(listingId)
                .ifPresentOrElse(listing -> {
                    log.info("Listing found by id. Attempting to delete!");
                    User user = userService.findRawUserById(userId);
                    user.setActiveListing(false);
                    user.getListings().remove(listing);
                    listingRepository.deleteById(listing.getId());
                    log.info("Listing deleted successfully!!");
                }, () -> log.error("Error occured while trying to delete listing!"));
    }

    @Override
    public ListingResponseDto update(ListingResponseDto listingResponseDto) {
        return null;
    }

    @Override
    @Transactional
    public ListingResponseDto addSignedUserToListing(UserListingUpdateRequestDto userListingUpdateRequestDto) {
        Listing listing = findRawById(userListingUpdateRequestDto.getListingId());
        User user = userService.findRawUserById(userListingUpdateRequestDto.getUserId());

        validateSignedUserWithListing(user, listing);

        listing.getSigned().add(user.getPlayer());
//        user.getListings().add(listing);

        return this.initSave(listing);
    }

    @Override
    @Transactional
    public ListingResponseDto deleteSignedUserFromListing(UserListingUpdateRequestDto userListingUpdateRequestDto) {
        Listing listing = findRawById(userListingUpdateRequestDto.getListingId());
        User user = userService.findRawUserById(userListingUpdateRequestDto.getUserId());

        if(listing.getSigned().isEmpty() && user.getListings().isEmpty())
            throw new IllegalParameterException("Listing signed users and user listings are empty lists!");

        listing.getSigned().remove(user.getPlayer());
//        user.getListings().remove(listing);

        return this.initSave(listing);
    }

    @Override
    @Transactional
    public ListingResponseDto deleteAcceptedUserFromListing(UserListingUpdateRequestDto userListingUpdateRequestDto) {
        Listing listing = findRawById(userListingUpdateRequestDto.getListingId());
        User user = userService.findRawUserById(userListingUpdateRequestDto.getUserId());

        if(listing.getAccepted().isEmpty() && user.getListings().isEmpty())
            throw new IllegalParameterException("Listing accepted users and user listings are empty lists!");

        listing.getAccepted().remove(user.getPlayer());
//        user.getListings().remove(listing);

        return this.initSave(listing);
    }

    @Override
    @Transactional
    public ListingResponseDto addAcceptedUserToListing(UserListingUpdateRequestDto userListingUpdateRequestDto) {
        Listing listing = listingRepository.findById(userListingUpdateRequestDto.getListingId())
                .orElseThrow(() -> new ObjectNotFoundException(Listing.class, userListingUpdateRequestDto.getListingId()));
        User user = userService.findRawUserById(userListingUpdateRequestDto.getUserId());

        validateAcceptedUserWithListing(user, listing);

        if (Objects.equals(listing.getSigned().size(), listing.getNeedPlayersCount())) {
            listing.getCreatedBy().setActiveListing(false);
            listing.setActive(false);
            return this.initSave(listing);
        }

        listing.getAccepted().add(user.getPlayer());
        listing.getSigned().remove(user.getPlayer());
//        user.getListings().add(listing);

        return this.initSave(listing);
    }

    @Override
    @Transactional
    public ListingResponseDto deactivateListing(Long listingId, Long userId) {
        Listing listing = findRawById(listingId);

        if(Boolean.FALSE.equals(listing.getActive()))
            throw new IllegalParameterException("Listing is already deactivated!");

        listing.setActive(false);
        User user = userService.findRawUserById(userId);
        user.setActiveListing(false);

        return this.initSave(listing);
    }

    private Listing findRawById(Long listingId) {
        return listingRepository.findById(listingId)
                .orElseThrow(() -> new ObjectNotFoundException(Listing.class, listingId));
    }

    private void validateAcceptedUserWithListing(User user, Listing listing) {
        if(listing.getAccepted().contains(user))
            throw new ObjectAlreadyExistsException("Listing already contains this user!");
        if(user.getListings().contains(listing))
            throw new ObjectAlreadyExistsException("User already contains this listing!");
    }

    private void validateSignedUserWithListing(User user, Listing listing) {
        if(listing.getSigned().contains(user))
            throw new ObjectAlreadyExistsException("Listing already contains this user!");
        if(user.getListings().contains(listing))
            throw new ObjectAlreadyExistsException("User already contains this listing!");
    }

    private ListingResponseDto mapUserToListing(Listing listing) {
        ListingResponseDto listingResponseDto = listingMapper.mapToResponseDto(listingRepository.save(listing));
        listingResponseDto.setCreatedBy(userMapper.mapToUserListingDto(listing.getCreatedBy()));
        listingResponseDto.setSigned(listing.getSigned());
        listingResponseDto.setAccepted(listing.getAccepted());
        return listingResponseDto;
    }
}
