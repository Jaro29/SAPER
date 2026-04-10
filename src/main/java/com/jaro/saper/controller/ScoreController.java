package com.jaro.saper.controller;

import com.jaro.saper.dto.RankingEntry;
import com.jaro.saper.dto.ScoreRequest;
import com.jaro.saper.dto.ScoreResponse;
import com.jaro.saper.model.Game.Difficulty;
import com.jaro.saper.model.User;
import com.jaro.saper.repository.UserRepository;
import com.jaro.saper.service.ScoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/scores")
@RequiredArgsConstructor
public class ScoreController {

    private final ScoreService scoreService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<ScoreResponse> saveScore(
            @Valid @RequestBody ScoreRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = getUser(userDetails);
        ScoreResponse response = scoreService.saveScore(user, request.getGameId(), request.getTimeSeconds());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/personal")
    public ResponseEntity<List<ScoreResponse>> getPersonal(
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = getUser(userDetails);
        return ResponseEntity.ok(scoreService.getPersonalScores(user));
    }

    @GetMapping("/global")
    public ResponseEntity<List<RankingEntry>> getGlobal(
            @RequestParam(required = false) Difficulty difficulty) {

        if (difficulty == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parametr difficulty jest wymagany");
        }
        return ResponseEntity.ok(scoreService.getGlobalRanking(difficulty));
    }

    private User getUser(UserDetails userDetails) {
        return userRepository.findByLogin(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Użytkownik nie istnieje"));
    }
}