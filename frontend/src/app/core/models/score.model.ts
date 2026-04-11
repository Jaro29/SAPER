import { Difficulty, GameStatus } from './game.model';

export interface ScoreResponse {
  scoreId: number;
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
