package com.jaro.saper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RankingEntry {

    private int rank;
    private String login;
    private Integer timeSeconds;
}