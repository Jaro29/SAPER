# Saper Web MVP — Progress

## Etapy

- ✅ Etap 1: Inicjalizacja projektu Spring Boot
- ✅ Etap 2: Encje JPA (User, Game, Score)
- ✅ Etap 3: Konfiguracja MariaDB + migracje Liquibase
- ✅ Etap 4: Autoryzacja JWT (rejestracja, logowanie, JWT filter, SecurityConfig)
- ✅ Etap 5: Logika gry (generowanie planszy, BFS, warunki zakończenia)
- ✅ Etap 6: Endpointy REST gry (reveal, flag, status)
- ✅ Etap 7: Endpointy wyników i rankingu
- ✅ Etap 8: Inicjalizacja frontendu (Angular 19, Tailwind CSS 4)
- ✅ Etap 9: Integracja frontend ↔ backend (logowanie, generowanie planszy, zapisywanie wyników)
- ⬜ Etap 10: Testy i weryfikacja MVP

## Ostatni krok

**Etap 9 zakończony** — merge do develop.  
Zrealizowane: integracja auth (logowanie, rejestracja, JWT interceptor, CORS), widok Lobby, widok gry z motywem kotów, zapisywanie wyników po wygranej, widok Rankingu z zakładkami global/personal (punkt 9.4 z PRD), naprawa routingu.  
Następny: **Etap 10** — testy i weryfikacja MVP (kryteria ukończenia z PRD §10).
