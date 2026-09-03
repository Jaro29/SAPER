import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { GameService } from '../../core/services/game.service';
import { GameResponse, CellDto } from '../../core/models/game.model';
import { ScoreService } from '../../core/services/score.service';
import { ScoreRequest } from '../../core/models/score.model';

@Component({
  selector: 'app-game',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.css'],
})
export class GameComponent implements OnInit, OnDestroy {
  game: GameResponse | null = null;
  errorMessage = '';
  loading = false;

  // Zwykła zmienna zamiast gettera!
  boardRows: CellDto[][] = [];

  secondsElapsed = 0;
  private timerInterval: any = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private gameService: GameService,
    private scoreService: ScoreService,
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.loadGame(Number(idParam));
    } else {
      this.router.navigate(['/lobby']);
    }
  }

  ngOnDestroy(): void {
    this.stopTimer();
  }

  loadGame(gameId: number): void {
    this.loading = true;
    this.gameService.getGame(gameId).subscribe({
      next: (data) => {
        this.game = data;
        this.updateBoard(); // Aktualizujemy planszę tylko po pobraniu danych
        this.loading = false;
        if (this.game.status === 'IN_PROGRESS' && this.game.cells && this.game.cells.length > 0) {
          this.startTimer();
        }
      },
      error: (err) => {
        this.errorMessage = 'Błąd podczas ładowania gry.';
        this.loading = false;
        console.error(err);
      },
    });
  }

  // Funkcja budująca planszę tylko wtedy, gdy ją o to poprosimy
  private updateBoard(): void {
    if (!this.game) return;

    const rows: CellDto[][] = [];
    if (!this.game.cells || this.game.cells.length === 0) {
      for (let r = 0; r < this.game.rows; r++) {
        const rowCells: CellDto[] = [];
        for (let c = 0; c < this.game.cols; c++) {
          rowCells.push({ row: r, col: c, state: 'HIDDEN', value: null });
        }
        rows.push(rowCells);
      }
      this.boardRows = rows;
      return;
    }

    for (let r = 0; r < this.game.rows; r++) {
      const rowCells = this.game.cells.filter((c) => c.row === r).sort((a, b) => a.col - b.col);
      rows.push(rowCells);
    }
    this.boardRows = rows;
  }

  onCellClick(cell: CellDto, event: MouseEvent): void {
    event.preventDefault();
    if (
      this.loading ||
      !this.game ||
      this.game.status !== 'IN_PROGRESS' ||
      cell.state === 'REVEALED' ||
      cell.state === 'FLAGGED'
    )
      return;

    if (!this.game.cells || this.game.cells.length === 0) {
      this.startTimer();
    }

    this.loading = true;
    this.gameService.revealCell(this.game.gameId, cell.row, cell.col).subscribe({
      next: (data) => {
        this.game = data;
        this.updateBoard(); // Aktualizujemy planszę po ruchu
        this.loading = false;
        this.checkGameEnd();
      },
      error: (err) => {
        console.error('Reveal error:', err);
        this.loading = false;
      },
    });
  }

  onCellRightClick(cell: CellDto, event: MouseEvent): void {
    event.preventDefault();
    if (
      this.loading ||
      !this.game ||
      this.game.status !== 'IN_PROGRESS' ||
      cell.state === 'REVEALED'
    )
      return;

    this.loading = true;
    this.gameService.flagCell(this.game.gameId, cell.row, cell.col).subscribe({
      next: (data) => {
        this.game = data;
        this.updateBoard(); // Aktualizujemy planszę po oflagowaniu
        this.loading = false;
      },
      error: (err) => {
        console.error('Flag error:', err);
        this.loading = false;
      },
    });
  }

  get minesRemaining(): number {
    if (!this.game) return 0;
    return this.game.mines - this.game.flagsPlaced;
  }

  get formattedTime(): string {
    const minutes = Math.floor(this.secondsElapsed / 60);
    const seconds = this.secondsElapsed % 60;
    return `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
  }

  private startTimer(): void {
    if (!this.timerInterval) {
      this.timerInterval = setInterval(() => {
        this.secondsElapsed++;
      }, 1000);
    }
  }

  private stopTimer(): void {
    if (this.timerInterval) {
      clearInterval(this.timerInterval);
      this.timerInterval = null;
    }
  }

  private checkGameEnd(): void {
    if (this.game && (this.game.status === 'WON' || this.game.status === 'LOST')) {
      this.stopTimer();
      if (this.game.status === 'WON') {
        const scoreReq: ScoreRequest = {
          gameId: this.game.gameId,
          timeSeconds: this.secondsElapsed,
        };
        this.scoreService.saveScore(scoreReq).subscribe({
          next: () => console.log(`Wygrana w czasie: ${this.secondsElapsed}s - Wynik zapisany!`),
          error: (err) => console.error('Błąd podczas zapisywania wyniku:', err),
        });
      }
    }
  }
}
