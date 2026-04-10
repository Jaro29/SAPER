package com.jaro.saper.dto;

import com.jaro.saper.model.Game.Difficulty;
import com.jaro.saper.model.Game.GameStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScoreResponse {

    private Long scoreId;
    private Long gameId;
    private Difficulty difficulty;
    private GameStatus status;
    private Integer timeSeconds;
    private LocalDateTime playedAt;
}