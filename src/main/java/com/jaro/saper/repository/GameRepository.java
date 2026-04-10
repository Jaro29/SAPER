package com.jaro.saper.repository;

import com.jaro.saper.model.Game;
import com.jaro.saper.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findByUser(User user);
}