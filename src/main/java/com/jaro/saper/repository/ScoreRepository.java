package com.jaro.saper.repository;

import com.jaro.saper.model.Game;
import com.jaro.saper.model.Game.Difficulty;
import com.jaro.saper.model.Score;
import com.jaro.saper.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

    boolean existsByGame(Game game);

    List<Score> findByUserOrderByPlayedAtDesc(User user);

    @Query("SELECT s FROM Score s WHERE s.difficulty = :difficulty ORDER BY s.timeSeconds ASC LIMIT 10")
    List<Score> findTop10ByDifficulty(Difficulty difficulty);
}