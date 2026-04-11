import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { GameResponse, MoveRequest, Difficulty } from '../models/game.model';

@Injectable({
  providedIn: 'root'
})
export class GameService {
  private readonly API = 'http://localhost:8080/api/games';

  constructor(private http: HttpClient) {}

  createGame(difficulty: Difficulty): Observable<GameResponse> {
    return this.http.post<GameResponse>(this.API, { difficulty });
  }

  getGame(gameId: number): Observable<GameResponse> {
    return this.http.get<GameResponse>(`${this.API}/${gameId}`);
  }

  revealCell(gameId: number, row: number, col: number): Observable<GameResponse> {
    const request: MoveRequest = { row, col };
    return this.http.post<GameResponse>(`${this.API}/${gameId}/reveal`, request);
  }

  flagCell(gameId: number, row: number, col: number): Observable<GameResponse> {
    const request: MoveRequest = { row, col };
    return this.http.post<GameResponse>(`${this.API}/${gameId}/flag`, request);
  }
}