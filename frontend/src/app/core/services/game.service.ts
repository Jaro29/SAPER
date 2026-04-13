import { Injectable, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { GameResponse, MoveRequest, Difficulty } from '../models/game.model';
import { API_URL } from '../../app.config';

@Injectable({ providedIn: 'root' })
export class GameService {
  constructor(
    private http: HttpClient,
    @Inject(API_URL) private apiUrl: string,
  ) {}

  createGame(difficulty: Difficulty): Observable<GameResponse> {
    return this.http.post<GameResponse>(`${this.apiUrl}/games`, { difficulty });
  }

  getGame(gameId: number): Observable<GameResponse> {
    return this.http.get<GameResponse>(`${this.apiUrl}/games/${gameId}`);
  }

  revealCell(gameId: number, row: number, col: number): Observable<GameResponse> {
    return this.http.post<GameResponse>(`${this.apiUrl}/games/${gameId}/reveal`, { row, col });
  }

  flagCell(gameId: number, row: number, col: number): Observable<GameResponse> {
    return this.http.post<GameResponse>(`${this.apiUrl}/games/${gameId}/flag`, { row, col });
  }
}
