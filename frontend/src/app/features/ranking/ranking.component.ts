import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ScoreService } from '../../core/services/score.service';
import { PersonalScore, RankingEntry } from '../../core/models/score.model';
import { Difficulty } from '../../core/models/game.model';

@Component({
  selector: 'app-ranking',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './ranking.component.html',
})
export class RankingComponent implements OnInit {
  activeTab: 'global' | 'personal' = 'global';
  selectedDifficulty: Difficulty = 'EASY';

  globalScores: RankingEntry[] = [];
  personalScores: PersonalScore[] = [];

  loading = false;
  errorMessage = '';

  constructor(private scoreService: ScoreService) {}

  ngOnInit(): void {
    this.loadGlobalRanking();
  }

  switchTab(tab: 'global' | 'personal'): void {
    this.activeTab = tab;
    if (tab === 'global') {
      this.loadGlobalRanking();
    } else {
      this.loadPersonalScores();
    }
  }

  setDifficulty(diff: Difficulty): void {
    this.selectedDifficulty = diff;
    if (this.activeTab === 'global') {
      this.loadGlobalRanking();
    }
  }

  loadGlobalRanking(): void {
    this.loading = true;
    this.errorMessage = '';
    this.scoreService.getGlobalRanking(this.selectedDifficulty).subscribe({
      next: (data) => {
        this.globalScores = data;
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Nie udało się pobrać rankingu globalnego.';
        this.loading = false;
      },
    });
  }

  loadPersonalScores(): void {
    this.loading = true;
    this.errorMessage = '';
    this.scoreService.getPersonalScores().subscribe({
      next: (data) => {
        this.personalScores = data;
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Nie udało się pobrać Twoich wyników.';
        this.loading = false;
      },
    });
  }

  formatTime(seconds: number): string {
    const minutes = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
  }
}
