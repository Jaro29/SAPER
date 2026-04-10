package com.jaro.saper.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScoreRequest {

    @NotNull
    private Long gameId;

    @NotNull
    @Min(1)
    private Integer timeSeconds;
}