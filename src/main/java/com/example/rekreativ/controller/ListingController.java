package com.example.rekreativ.controller;

import com.example.rekreativ.dto.request.ListingRequestDto;
import com.example.rekreativ.dto.request.ReviewRequestDto;
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
}
