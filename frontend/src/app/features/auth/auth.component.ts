import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { AuthRequest } from '../../core/models/auth.model';

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './auth.component.html',
})
export class AuthComponent {
  mode: 'login' | 'register' = 'login';
  login = '';
  password = '';
  errorMessage = '';
  loading = false;

  constructor(
    private authService: AuthService,
    private router: Router,
  ) {}

  toggleMode(): void {
    this.mode = this.mode === 'login' ? 'register' : 'login';
    this.errorMessage = '';
  }

  submit(): void {
    this.errorMessage = '';
    this.loading = true;

    const request: AuthRequest = { login: this.login, password: this.password };

    const action$ =
      this.mode === 'login' ? this.authService.login(request) : this.authService.register(request);

    action$.subscribe({
      next: () => this.router.navigate(['/lobby']),
      error: (err) => {
        this.errorMessage = err.error?.message ?? 'Wystąpił błąd. Spróbuj ponownie.';
        this.loading = false;
      },
    });
  }
}
