import { Injectable, Inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL } from '../../app.config';
import { ScoreRequest, ScoreResponse, PersonalScore, RankingEntry } from '../models/score.model';
import { Difficulty } from '../models/game.model';

@Injectable({ providedIn: 'root' })
export class ScoreService {
  constructor(
    private http: HttpClient,
    @Inject(API_URL) private apiUrl: string,
  ) {}

  saveScore(request: ScoreRequest): Observable<ScoreResponse> {
    return this.http.post<ScoreResponse>(`${this.apiUrl}/scores`, request);
  }

  getPersonalScores(): Observable<PersonalScore[]> {
    return this.http.get<PersonalScore[]>(`${this.apiUrl}/scores/personal`);
  }

  getGlobalRanking(difficulty: Difficulty): Observable<RankingEntry[]> {
    const params = new HttpParams().set('difficulty', difficulty);
    return this.http.get<RankingEntry[]>(`${this.apiUrl}/scores/global`, { params });
  }
}
