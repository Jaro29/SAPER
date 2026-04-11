# Saper Web MVP — Progress

## Stack

Java 21 + Spring Boot 3.5 + Spring Security + JWT + MariaDB + Angular 19 + TypeScript + Tailwind CSS 4

## Etapy

- [x] Etap 1: Inicjalizacja projektu Spring Boot
- [x] Etap 2: Encje JPA (User, Game, Score)
- [x] Etap 3: Konfiguracja MariaDB + migracje Liquibase
- [x] Etap 4: Autoryzacja JWT (rejestracja, logowanie, JWT filter, SecurityConfig)
- [x] Etap 5: Logika gry (generowanie planszy, BFS, warunki zakończenia)
- [x] Etap 6: Endpointy REST gry (reveal, flag, status)
- [x] Etap 7: Endpointy wyników i rankingu
- [x] Etap 8: Inicjalizacja frontendu (Angular 19, Vite, Tailwind CSS 4)
- [ ] Etap 9: Integracja frontend ↔ backend (logowanie, generowanie planszy)
- [ ] Etap 10: Testy i weryfikacja MVP

## Ostatni krok

W trakcie Etapu 9 — ukończono integrację autoryzacji (logowanie, rejestracja, JWT interceptor, CORS).
Następny krok: widok Lobby (wybór poziomu trudności).
