package com.jaro.saper.dto;

import com.jaro.saper.model.Game.Difficulty;
import com.jaro.saper.model.Game.GameStatus;
import lombok.Data;

import java.util.List;

@Data
public class GameResponse {

    private Long gameId;
    private GameStatus status;
    private Difficulty difficulty;
    private int rows;
    private int cols;
    private int mines;
    private int flagsPlaced;
    private List<CellDto> cells;
}