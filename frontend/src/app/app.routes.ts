import { Routes } from '@angular/router';
import { AuthComponent } from './features/auth/auth.component';
import { LobbyComponent } from './features/lobby/lobby.component';

export const routes: Routes = [
  { path: '', redirectTo: 'auth', pathMatch: 'full' },
  { path: 'auth', component: AuthComponent },
  { path: 'lobby', component: LobbyComponent },
  { path: '**', redirectTo: 'auth' }
];