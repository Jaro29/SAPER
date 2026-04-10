package com.jaro.saper.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MoveRequest {

    @NotNull
    @Min(0)
    private Integer row;

    @NotNull
    @Min(0)
    private Integer col;
}