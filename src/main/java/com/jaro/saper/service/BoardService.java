package com.jaro.saper.service;

import com.jaro.saper.model.Cell;
import com.jaro.saper.model.Cell.CellState;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BoardService {

    private static final int[][] DIRECTIONS = {
            {-1,-1}, {-1,0}, {-1,1},
            { 0,-1},         { 0,1},
            { 1,-1}, { 1,0}, { 1,1}
    };

    // Generuje planszę po pierwszym kliknięciu
    public Cell[][] generateBoard(int rows, int cols, int mines, int firstRow, int firstCol) {
        Cell[][] board = new Cell[rows][cols];

        // Inicjalizacja wszystkich komórek
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                board[r][c] = new Cell(0, CellState.HIDDEN);
            }
        }

        // Strefa bezpieczna — kliknięta komórka + sąsiedzi (3x3)
        Set<String> safeZone = new HashSet<>();
        for (int[] dir : DIRECTIONS) {
            int sr = firstRow + dir[0];
            int sc = firstCol + dir[1];
            if (inBounds(sr, sc, rows, cols)) {
                safeZone.add(sr + "," + sc);
            }
        }
        safeZone.add(firstRow + "," + firstCol);

        // Losowanie pozycji min poza strefą bezpieczną
        List<String> candidates = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (!safeZone.contains(r + "," + c)) {
                    candidates.add(r + "," + c);
                }
            }
        }

        Collections.shuffle(candidates);
        for (int i = 0; i < mines; i++) {
            String[] pos = candidates.get(i).split(",");
            board[Integer.parseInt(pos[0])][Integer.parseInt(pos[1])].setValue(-1);
        }

        // Obliczanie wartości komórek (0-8)
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (!board[r][c].isMine()) {
                    board[r][c].setValue(countAdjacentMines(board, r, c, rows, cols));
                }
            }
        }

        return board;
    }

    // BFS — lawinowe odkrywanie
    public void reveal(Cell[][] board, int row, int col, int rows, int cols) {
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{row, col});
        board[row][col].setState(CellState.REVEALED);

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int r = current[0];
            int c = current[1];

            if (board[r][c].isEmpty()) {
                for (int[] dir : DIRECTIONS) {
                    int nr = r + dir[0];
                    int nc = c + dir[1];
                    if (inBounds(nr, nc, rows, cols) &&
                            board[nr][nc].getState() == CellState.HIDDEN) {
                        board[nr][nc].setState(CellState.REVEALED);
                        queue.add(new int[]{nr, nc});
                    }
                }
            }
        }
    }

    // Sprawdza warunek wygranej
    public boolean isWon(Cell[][] board, int rows, int cols, int mines) {
        int revealed = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board[r][c].getState() == CellState.REVEALED) {
                    revealed++;
                }
            }
        }
        return revealed == (rows * cols) - mines;
    }

    private int countAdjacentMines(Cell[][] board, int row, int col, int rows, int cols) {
        int count = 0;
        for (int[] dir : DIRECTIONS) {
            int nr = row + dir[0];
            int nc = col + dir[1];
            if (inBounds(nr, nc, rows, cols) && board[nr][nc].isMine()) {
                count++;
            }
        }
        return count;
    }

    private boolean inBounds(int row, int col, int rows, int cols) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }
}