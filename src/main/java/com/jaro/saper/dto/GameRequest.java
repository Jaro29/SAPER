package com.jaro.saper.dto;

import com.jaro.saper.model.Game.Difficulty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GameRequest {

    @NotNull(message = "Poziom trudności jest wymagany")
    private Difficulty difficulty;
}