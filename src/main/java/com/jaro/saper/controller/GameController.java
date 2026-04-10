package com.jaro.saper.controller;

import com.jaro.saper.dto.GameMapper;
import com.jaro.saper.dto.GameRequest;
import com.jaro.saper.dto.GameResponse;
import com.jaro.saper.dto.MoveRequest;
import com.jaro.saper.model.Game;
import com.jaro.saper.model.User;
import com.jaro.saper.repository.UserRepository;
import com.jaro.saper.service.GameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final GameMapper gameMapper;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<GameResponse> createGame(
            @Valid @RequestBody GameRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = getUser(userDetails);
        Game game = gameService.createGame(user, request.getDifficulty());
        return ResponseEntity.status(HttpStatus.CREATED).body(gameMapper.toResponse(game));
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameResponse> getGame(
            @PathVariable Long gameId,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = getUser(userDetails);
        Game game = gameService.getGameById(gameId, user);
        return ResponseEntity.ok(gameMapper.toResponse(game));
    }

    @PostMapping("/{gameId}/reveal")
    public ResponseEntity<GameResponse> reveal(
            @PathVariable Long gameId,
            @Valid @RequestBody MoveRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = getUser(userDetails);
        Game game = gameService.reveal(gameId, user, request.getRow(), request.getCol());
        return ResponseEntity.ok(gameMapper.toResponse(game));
    }

    @PostMapping("/{gameId}/flag")
    public ResponseEntity<GameResponse> flag(
            @PathVariable Long gameId,
            @Valid @RequestBody MoveRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = getUser(userDetails);
        Game game = gameService.flag(gameId, user, request.getRow(), request.getCol());
        return ResponseEntity.ok(gameMapper.toResponse(game));
    }

    private User getUser(UserDetails userDetails) {
        return userRepository.findByLogin(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Użytkownik nie istnieje"));
    }
}