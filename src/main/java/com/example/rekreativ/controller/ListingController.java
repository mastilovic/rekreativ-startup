package com.example.rekreativ.controller;

import com.example.rekreativ.dto.request.ListingRequestDto;
import com.example.rekreativ.service.ListingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
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
    @PostMapping("/signed")
    public ResponseEntity<?> addSignedUserToListing(@RequestParam(name = "userId") Long userId,
                                                    @RequestParam(name = "listingId") Long listingId) {
        return ResponseEntity.ok(listingService.addSignedUserToListing(listingId, userId));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @DeleteMapping("/signed")
    public ResponseEntity<?> deleteSignedUserFromListing(@RequestParam(name = "userId") Long userId,
                                                         @RequestParam(name = "listingId") Long listingId) {
        return ResponseEntity.ok(listingService.deleteSignedUserFromListing(listingId, userId));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @PostMapping("/accepted")
    public ResponseEntity<?> addAcceptedUserToListing(@RequestParam(name = "userId") Long userId,
                                                      @RequestParam(name = "listingId") Long listingId) {
        return ResponseEntity.ok(listingService.addAcceptedUserToListing(listingId, userId));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @DeleteMapping("/accepted")
    public ResponseEntity<?> deleteAcceptedUserFromListing(@RequestParam(name = "userId") Long userId,
                                                           @RequestParam(name = "listingId") Long listingId) {
        return ResponseEntity.ok(listingService.deleteAcceptedUserFromListing(listingId, userId));
    }
}
