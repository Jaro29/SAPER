package com.jaro.saper.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaro.saper.model.Cell;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardSerializer {

    private final ObjectMapper objectMapper;

    public String serialize(Cell[][] board) {
        try {
            return objectMapper.writeValueAsString(board);
        } catch (Exception e) {
            throw new RuntimeException("Błąd serializacji planszy", e);
        }
    }

    public Cell[][] deserialize(String json, int rows, int cols) {
        try {
            return objectMapper.readValue(json, Cell[][].class);
        } catch (Exception e) {
            throw new RuntimeException("Błąd deserializacji planszy", e);
        }
    }
}
