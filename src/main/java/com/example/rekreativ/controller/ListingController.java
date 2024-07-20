package com.example.rekreativ.controller;

import com.example.rekreativ.model.dto.request.ListingRequestDto;
import com.example.rekreativ.model.dto.request.PlayerTypeRequestDto;
import com.example.rekreativ.model.dto.request.UserListingUpdateRequestDto;
import com.example.rekreativ.model.enums.PlayerType;
import com.example.rekreativ.service.ListingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/listing")
@Slf4j
public class ListingController {

    private final ListingService listingService;

    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createListing(@RequestBody ListingRequestDto review) {
        return ResponseEntity.ok(listingService.save(review));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(listingService.findById(id));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(listingService.findAll());
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam(name = "userId") Long userId,
                                    @RequestParam(name = "listingId") Long listingId) {
        listingService.delete(listingId, userId);
        return ResponseEntity.ok().build();
    }
    //TODO: test endpoints with new request body
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @PostMapping("/signed")
    public ResponseEntity<?> addSignedUserToListing(@RequestBody UserListingUpdateRequestDto userListingUpdateRequestDto) {
        return ResponseEntity.ok(listingService.addSignedUserToListing(userListingUpdateRequestDto));
    }
    //TODO: test endpoints with new request body
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @DeleteMapping("/signed")
    public ResponseEntity<?> deleteSignedUserFromListing(@RequestBody UserListingUpdateRequestDto userListingUpdateRequestDto) {
        return ResponseEntity.ok(listingService.deleteSignedUserFromListing(userListingUpdateRequestDto));
    }

    //TODO: test endpoints with new request body
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @PostMapping("/accepted")
    public ResponseEntity<?> addAcceptedUserToListing(@RequestBody UserListingUpdateRequestDto userListingUpdateRequestDto) {
        return ResponseEntity.ok(listingService.addAcceptedUserToListing(userListingUpdateRequestDto));
    }
    //TODO: test endpoints with new request body
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @DeleteMapping("/accepted")
    public ResponseEntity<?> deleteAcceptedUserFromListing(@RequestBody UserListingUpdateRequestDto userListingUpdateRequestDto) {
        return ResponseEntity.ok(listingService.deleteAcceptedUserFromListing(userListingUpdateRequestDto));
    }
    //TODO: test endpoints with new request body
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @PostMapping("/deactivate")
    public ResponseEntity<?> deactivateListing(@RequestParam(name = "listingId") Long listingId,
                                               @RequestParam(name = "userId") Long userId) {
        return ResponseEntity.ok(listingService.deactivateListing(listingId, userId));
    }
}
