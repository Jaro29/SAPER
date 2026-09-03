import { Difficulty, GameStatus } from './game.model';

export interface ScoreRequest {
  gameId: number;
  timeSeconds: number;
}

export interface ScoreResponse {
  scoreId: number;
  timeSeconds: number;
}

export interface PersonalScore {
  gameId: number;
  difficulty: Difficulty;
  status: GameStatus;
  timeSeconds: number;
  playedAt: string;
}

export interface RankingEntry {
  rank: number;
  login: string;
  timeSeconds: number;
}
