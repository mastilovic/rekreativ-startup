package com.example.rekreativ.service.impl;

import com.example.rekreativ.error.exceptions.IllegalParameterException;
import com.example.rekreativ.error.exceptions.ObjectNotFoundException;
import com.example.rekreativ.mapper.PlayerMapper;
import com.example.rekreativ.mapper.ReviewMapper;
import com.example.rekreativ.model.Player;
import com.example.rekreativ.model.PlayerCharacteristics;
import com.example.rekreativ.model.Review;
import com.example.rekreativ.model.dto.request.PlayerUpdateRequestDto;
import com.example.rekreativ.model.dto.request.ReviewRequestDto;
import com.example.rekreativ.model.dto.response.PlayerResponseDto;
import com.example.rekreativ.repository.PlayerRepository;
import com.example.rekreativ.service.PlayerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;
    private final ReviewMapper reviewMapper;

    public PlayerServiceImpl(PlayerRepository playerRepository,
                             PlayerMapper playerMapper,
                             ReviewMapper reviewMapper) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
        this.reviewMapper = reviewMapper;
    }

    // fixme: probably only for internal use as player is managed through @User
    @Override
    public Player save(Player player) {
        return playerRepository.save(player);
    }

    @Override
    public PlayerResponseDto findById(Long id) {
        return playerRepository.findById(id)
                .map(playerMapper::mapToPlayerResponseDto)
                .orElseThrow(() -> new ObjectNotFoundException(Player.class, id));
    }

    @Override
    public Player findRawById(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(Player.class, id));
    }

    @Override
    public List<PlayerResponseDto> findAll() {
        return playerRepository.findAll()
                .stream()
                .map(playerMapper::mapToPlayerResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public PlayerResponseDto addReviewToPlayer(Long playerId, ReviewRequestDto reviewRequestDto) {

        validateReviewRequestDto(reviewRequestDto);

        Player player = findRawById(playerId);
        player.getReviews().add(reviewMapper.mapToReview(reviewRequestDto));

        return playerMapper.mapToPlayerResponseDto(save(player));
    }


    // fixme: probably only for internal use as player is managed through @User
    @Override
    public void delete(Long id) {
        playerRepository.deleteById(id);
    }

    @Override
    public PlayerResponseDto update(PlayerUpdateRequestDto playerRequest) {
        Player player = findRawById(playerRequest.getId());

        if (Objects.isNull(player.getPlayerCharacteristics()))
            player.setPlayerCharacteristics(new PlayerCharacteristics());

        PlayerCharacteristics playerCharacteristics = player.getPlayerCharacteristics();
        playerCharacteristics.setPlayerType(StringUtils.isBlank(playerRequest.getPlayerType())
                                            ? playerCharacteristics.getPlayerType()
                                            : playerRequest.getPlayerType());
        playerCharacteristics.setFullName(StringUtils.isBlank(playerRequest.getFullName())
                                          ? playerCharacteristics.getFullName()
                                          : playerRequest.getFullName());
        player.setPlayerCharacteristics(playerCharacteristics);

        return playerMapper.mapToPlayerResponseDto(playerRepository.save(player));
    }

    private void validateReviewRequestDto(ReviewRequestDto reviewRequestDto) {
        if(Objects.isNull(reviewRequestDto))
            throw new ObjectNotFoundException(Review.class, "");
        if(Objects.isNull(reviewRequestDto.getRating()))
            throw new ObjectNotFoundException(Review.class, "");
        if(reviewRequestDto.getRating() < 1 || reviewRequestDto.getRating() > 5)
            throw new IllegalParameterException("Review rating can't be below 1 or above 5!");
        if(StringUtils.isBlank(reviewRequestDto.getTitle()))
            throw new IllegalParameterException("Title can't be empty.");
        //reviewRequestDto.getDescription() can be empty
    }
}
