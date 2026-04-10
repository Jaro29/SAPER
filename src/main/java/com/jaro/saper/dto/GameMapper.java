package com.jaro.saper.dto;

import com.jaro.saper.model.Cell;
import com.jaro.saper.model.Cell.CellState;
import com.jaro.saper.model.Game;
import com.jaro.saper.model.Game.GameStatus;
import com.jaro.saper.service.BoardSerializer;
import com.jaro.saper.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GameMapper {

    private final BoardSerializer boardSerializer;
    private final GameService gameService;

    public GameResponse toResponse(Game game) {
        int[] dims = gameService.getDims(game.getDifficulty());
        int rows = dims[0], cols = dims[1], mines = dims[2];

        GameResponse response = new GameResponse();
        response.setGameId(game.getId());
        response.setStatus(game.getStatus());
        response.setDifficulty(game.getDifficulty());
        response.setRows(rows);
        response.setCols(cols);
        response.setMines(mines);

        if (game.getBoardState() == null) {
            response.setCells(List.of());
            response.setFlagsPlaced(0);
            return response;
        }

        Cell[][] board = boardSerializer.deserialize(game.getBoardState(), rows, cols);
        boolean gameOver = game.getStatus() == GameStatus.WON
                || game.getStatus() == GameStatus.LOST;

        List<CellDto> cells = new ArrayList<>();
        int flags = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = board[r][c];
                CellState state = cell.getState();

                if (state == CellState.FLAGGED) flags++;

                // Wartość ukryta dla HIDDEN i FLAGGED podczas trwającej gry
                Integer value = null;
                if (state == CellState.REVEALED || gameOver) {
                    value = cell.getValue();
                }

                cells.add(new CellDto(r, c, state, value));
            }
        }

        response.setFlagsPlaced(flags);
        response.setCells(cells);
        return response;
    }
}