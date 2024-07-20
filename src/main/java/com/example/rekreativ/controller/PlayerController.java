package com.example.rekreativ.controller;

import com.example.rekreativ.model.Player;
import com.example.rekreativ.model.dto.request.ListingRequestDto;
import com.example.rekreativ.model.dto.request.PlayerUpdateRequestDto;
import com.example.rekreativ.model.dto.request.ReviewRequestDto;
import com.example.rekreativ.model.dto.response.PlayerResponseDto;
import com.example.rekreativ.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/player")
@Slf4j
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<PlayerResponseDto> updatePlayerInfo(@RequestBody PlayerUpdateRequestDto player) {
        return ResponseEntity.ok(playerService.update(player));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/{playerId}")
    public ResponseEntity<PlayerResponseDto> getPlayerById(@PathVariable Long playerId) {
        return ResponseEntity.ok(playerService.findById(playerId));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<PlayerResponseDto>> getPlayers() {
        return ResponseEntity.ok(playerService.findAll());
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @PostMapping("/review/{playerId}")
    public ResponseEntity<PlayerResponseDto> addReviewToPlayer(@PathVariable Long playerId,
                                                               @RequestBody ReviewRequestDto reviewRequestDto) {
        return ResponseEntity.ok(playerService.addReviewToPlayer(playerId, reviewRequestDto));
    }
}
