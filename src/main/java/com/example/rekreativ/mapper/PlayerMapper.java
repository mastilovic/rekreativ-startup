package com.example.rekreativ.mapper;

import com.example.rekreativ.model.Player;
import com.example.rekreativ.model.dto.response.PlayerResponseDto;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PlayerMapper {

    private final ReviewMapper reviewMapper;

    public PlayerMapper(ReviewMapper reviewMapper) {
        this.reviewMapper = reviewMapper;
    }

    public PlayerResponseDto mapToPlayerResponseDto(Player player){
        PlayerResponseDto playerResponseDto = new PlayerResponseDto();

        playerResponseDto.setId(player.getId());
        playerResponseDto.setPlayerCharacteristics(player.getPlayerCharacteristics());
        playerResponseDto.setReviews(player.getReviews()
                                             .stream()
                                             .map(reviewMapper::mapToReviewResponseDto)
                                             .collect(Collectors.toList()));
        playerResponseDto.setUserId(player.getUser().getId());

        return playerResponseDto;
    }
}
