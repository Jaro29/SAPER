import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { GameService } from '../../core/services/game.service';
import { Difficulty } from '../../core/models/game.model';

@Component({
  selector: 'app-lobby',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './lobby.component.html'
})
export class LobbyComponent {
  loading = false;
  errorMessage = '';

  levels: { difficulty: Difficulty; label: string; desc: string; color: string }[] = [
    { difficulty: 'EASY', label: 'Łatwy', desc: '9×9 / 10 pudełek', color: 'emerald' },
    { difficulty: 'MEDIUM', label: 'Średni', desc: '16×16 / 40 pudełek', color: 'yellow' },
    { difficulty: 'HARD', label: 'Trudny', desc: '30×16 / 99 pudełek', color: 'red' }
  ];

  constructor(
    private router: Router,
    private authService: AuthService,
    private gameService: GameService
  ) {}

  startGame(difficulty: Difficulty): void {
    this.loading = true;
    this.errorMessage = '';

    this.gameService.createGame(difficulty).subscribe({
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