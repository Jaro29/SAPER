import { Routes } from '@angular/router';
import { AuthComponent } from './features/auth/auth.component';
import { LobbyComponent } from './features/lobby/lobby.component';
import { GameComponent } from './features/game/game.component';

export const routes: Routes = [
  { path: '', redirectTo: 'auth', pathMatch: 'full' },
  { path: 'auth', component: AuthComponent },
  { path: 'lobby', component: LobbyComponent },
  { path: 'game/:id', component: GameComponent },
  { path: '**', redirectTo: 'auth' }
];