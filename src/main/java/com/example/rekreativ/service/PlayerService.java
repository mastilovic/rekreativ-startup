package com.example.rekreativ.service;

import com.example.rekreativ.model.Player;
import com.example.rekreativ.model.dto.request.PlayerUpdateRequestDto;
import com.example.rekreativ.model.dto.request.ReviewRequestDto;
import com.example.rekreativ.model.dto.response.PlayerResponseDto;

import java.util.List;

public interface PlayerService {
    Player save(Player player);
    PlayerResponseDto findById(Long id);
    Player findRawById(Long id);
    List<PlayerResponseDto> findAll();
    void delete(Long id);
    PlayerResponseDto addReviewToPlayer(Long playerId, ReviewRequestDto reviewRequestDto);
    PlayerResponseDto update(PlayerUpdateRequestDto playerRequest);
}
