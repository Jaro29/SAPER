package com.jaro.saper.dto;

import com.jaro.saper.model.Cell.CellState;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CellDto {

    private int row;
    private int col;
    private CellState state;
    private Integer value; // null dla HIDDEN i FLAGGED podczas trwającej gry
}