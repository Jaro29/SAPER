export type Difficulty = 'EASY' | 'MEDIUM' | 'HARD';
export type GameStatus = 'IN_PROGRESS' | 'WON' | 'LOST' | 'ABANDONED';
export type CellState = 'HIDDEN' | 'REVEALED' | 'FLAGGED';

export interface CellDto {
  row: number;
  col: number;
  state: CellState;
  value: number | null;
}

export interface GameResponse {
  gameId: number;
  status: GameStatus;
  difficulty: Difficulty;
  rows: number;
  cols: number;
  mines: number;
  flagsPlaced: number;
  cells: CellDto[];
}

export interface MoveRequest {
  row: number;
  col: number;
}
