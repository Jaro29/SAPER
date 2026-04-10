package com.jaro.saper.service;

import com.jaro.saper.dto.RankingEntry;
import com.jaro.saper.dto.ScoreResponse;
import com.jaro.saper.model.Game;
import com.jaro.saper.model.Game.Difficulty;
import com.jaro.saper.model.Game.GameStatus;
import com.jaro.saper.model.Score;
import com.jaro.saper.model.User;
import com.jaro.saper.repository.GameRepository;
import com.jaro.saper.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreService {

    private final ScoreRepository scoreRepository;
    private final GameRepository gameRepository;

    public ScoreResponse saveScore(User user, Long gameId, Integer timeSeconds) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gra nie istnieje"));

        if (!game.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Brak dostępu do tej gry");
        }

        if (game.getStatus() != GameStatus.WON) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Wynik można zapisać tylko dla wygranej gry");
        }

        if (scoreRepository.existsByGame(game)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Wynik dla tej gry już istnieje");
        }

        Score score = new Score();
        score.setUser(user);
        score.setGame(game);
        score.setDifficulty(game.getDifficulty());
        score.setTimeSeconds(timeSeconds);
        scoreRepository.save(score);

        return toResponse(score);
    }

    public List<ScoreResponse> getPersonalScores(User user) {
        return scoreRepository.findByUserOrderByPlayedAtDesc(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<RankingEntry> getGlobalRanking(Difficulty difficulty) {
        List<Score> scores = scoreRepository.findTop10ByDifficulty(difficulty);
        List<RankingEntry> ranking = new ArrayList<>();
        for (int i = 0; i < scores.size(); i++) {
            Score s = scores.get(i);
            ranking.add(new RankingEntry(i + 1, s.getUser().getLogin(), s.getTimeSeconds()));
        }
        return ranking;
    }

    private ScoreResponse toResponse(Score score) {
        ScoreResponse response = new ScoreResponse();
        response.setScoreId(score.getId());
        response.setGameId(score.getGame().getId());
        response.setDifficulty(score.getDifficulty());
        response.setStatus(score.getGame().getStatus());
        response.setTimeSeconds(score.getTimeSeconds());
        response.setPlayedAt(score.getPlayedAt());
        return response;
    }
}