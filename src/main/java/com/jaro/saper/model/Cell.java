package com.jaro.saper.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cell {

    private int value;        // -1 = mina, 0-8 = liczba sąsiednich min
    private CellState state;  // stan komórki widoczny dla gracza

    public enum CellState {
        HIDDEN, REVEALED, FLAGGED
    }

    public boolean isMine() {
        return value == -1;
    }

    public boolean isEmpty() {
        return value == 0;
    }
}