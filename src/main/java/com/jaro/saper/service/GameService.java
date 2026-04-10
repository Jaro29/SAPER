package com.jaro.saper.service;

import com.jaro.saper.model.Cell;
import com.jaro.saper.model.Cell.CellState;
import com.jaro.saper.model.Game;
import com.jaro.saper.model.Game.Difficulty;
import com.jaro.saper.model.Game.GameStatus;
import com.jaro.saper.model.User;
import com.jaro.saper.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final BoardService boardService;
    private final BoardSerializer boardSerializer;

    private static final int[] ROWS  = {9, 16, 30};
    private static final int[] COLS  = {9, 16, 16};
    private static final int[] MINES = {10, 40, 99};

    public Game createGame(User user, Difficulty difficulty) {
        Game game = new Game();
        game.setUser(user);
        game.setDifficulty(difficulty);
        return gameRepository.save(game);
    }

    public Game reveal(Long gameId, User user, int row, int col) {
        Game game = getGameForUser(gameId, user);

        int[] dims = getDims(game.getDifficulty());
        int rows = dims[0], cols = dims[1], mines = dims[2];

        // Walidacja koordynatów
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Koordynaty poza planszą");
        }

        Cell[][] board;

        // Pierwszy klik — generujemy planszę
        if (game.getBoardState() == null) {
            board = boardService.generateBoard(rows, cols, mines, row, col);
        } else {
            board = boardSerializer.deserialize(game.getBoardState(), rows, cols);
        }

        Cell cell = board[row][col];

        if (cell.getState() == CellState.REVEALED) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Komórka już odkryta");
        }

        // Odkrycie miny — przegrana
        if (cell.isMine()) {
            revealAllMines(board, rows, cols);
            game.setBoardState(boardSerializer.serialize(board));
            game.setStatus(GameStatus.LOST);
            game.setFinishedAt(LocalDateTime.now());
            return gameRepository.save(game);
        }

        // BFS — odkrycie komórki (i lawina jeśli value == 0)
        boardService.reveal(board, row, col, rows, cols);

        game.setBoardState(boardSerializer.serialize(board));

        // Sprawdzenie wygranej
        if (boardService.isWon(board, rows, cols, mines)) {
            game.setStatus(GameStatus.WON);
            game.setFinishedAt(LocalDateTime.now());
        }

        return gameRepository.save(game);
    }

    public Game flag(Long gameId, User user, int row, int col) {
        Game game = getGameForUser(gameId, user);

        int[] dims = getDims(game.getDifficulty());
        int rows = dims[0], cols = dims[1];

        if (game.getBoardState() == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Gra jeszcze nie rozpoczęta");
        }

        Cell[][] board = boardSerializer.deserialize(game.getBoardState(), rows, dims[2]);
        Cell cell = board[row][col];

        if (cell.getState() == CellState.REVEALED) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Nie można oflagować odkrytej komórki");
        }

        // Toggle flagi
        if (cell.getState() == CellState.FLAGGED) {
            cell.setState(CellState.HIDDEN);
        } else {
            cell.setState(CellState.FLAGGED);
        }

        game.setBoardState(boardSerializer.serialize(board));
        return gameRepository.save(game);
    }

    public Game getGameById(Long gameId, User user) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gra nie istnieje"));

        if (!game.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Brak dostępu do tej gry");
        }

        return game;
    }

    public Game getGameForUser(Long gameId, User user) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gra nie istnieje"));

        if (!game.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Brak dostępu do tej gry");
        }

        if (game.getStatus() != GameStatus.IN_PROGRESS) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Gra już zakończona");
        }

        return game;
    }

    private void revealAllMines(Cell[][] board, int rows, int cols) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board[r][c].isMine()) {
                    board[r][c].setState(CellState.REVEALED);
                }
            }
        }
    }

    public int[] getDims(Difficulty difficulty) {
        return switch (difficulty) {
            case EASY   -> new int[]{ROWS[0], COLS[0], MINES[0]};
            case MEDIUM -> new int[]{ROWS[1], COLS[1], MINES[1]};
            case HARD   -> new int[]{ROWS[2], COLS[2], MINES[2]};
        };
    }
}