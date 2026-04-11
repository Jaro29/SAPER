import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../core/services/auth.service';
import { GameResponse } from '../../core/models/game.model';

@Component({
  selector: 'app-lobby',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './lobby.component.html'
})
export class LobbyComponent {
  loading = false;
  errorMessage = '';

  levels = [
    { difficulty: 'EASY',   label: 'Łatwy',  desc: '9×9 / 10 min',   color: 'emerald' },
    { difficulty: 'MEDIUM', label: 'Średni', desc: '16×16 / 40 min',  color: 'yellow'  },
    { difficulty: 'HARD',   label: 'Trudny', desc: '30×16 / 99 min',  color: 'red'     }
  ];

  constructor(
    private http: HttpClient,
    private router: Router,
    private authService: AuthService
  ) {}

  startGame(difficulty: string): void {
    this.loading = true;
    this.errorMessage = '';

    this.http.post<GameResponse>('http://localhost:8080/api/games', { difficulty })
      .subscribe({
        next: (game) => this.router.navigate(['/game', game.gameId]),
        error: () => {
          this.errorMessage = 'Nie udało się utworzyć gry. Spróbuj ponownie.';
          this.loading = false;
        }
      });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/auth']);
  }
}