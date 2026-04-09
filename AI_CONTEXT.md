# AI_CONTEXT — Saper Web MVP

## Cel projektu
Webowa gra Saper z logiką po stronie serwera. Autoryzacja JWT, zapis wyników, ranking globalny.
PRD: `Saper_PRD.md` — zawiera pełne API, format JSON, algorytm BFS, kryteria MVP.
## Cel projektu
Webowa gra Saper z logiką po stronie serwera. Autoryzacja JWT, zapis wyników, ranking globalny.
PRD: plik `Saper_PRD.md` w repozytorium (do dodania).

## Repozytorium
https://github.com/Jaro29/SAPER.git

## Branch strategy
main → develop → feature/etap-XX-nazwa
- feature branch per etap → merge do develop po ukończeniu → main po MVP

## Stack
- Backend: Java 21 + Spring Boot 3.5.x + Spring Security + JWT + Liquibase
- Baza: MariaDB 12.x (nie MySQL!) — sterownik `mariadb-java-client`
- Frontend: TypeScript + Tailwind CSS (etap 8+)
- IDE: IntelliJ IDEA, System: CachyOS Linux

## Struktura pakietów i encje

```
com.jaro.saper
├── model
│   ├── User.java        → id, login, passwordHash, createdAt
│   ├── Game.java        → id, user, status(enum), difficulty(enum), boardState(JSON), createdAt, finishedAt
│   │                       Enums zagnieżdżone: Game.GameStatus, Game.Difficulty
│   └── Score.java       → id, user, game(UNIQUE), difficulty, timeSeconds, playedAt
├── repository           # interfejsy Spring Data
├── controller           # endpointy REST (do dodania)
├── security             # AuthService
│   ├── config           # SecurityConfig 
│   ├── dto              # obiekty żądań/odpowiedzi
│   └── jwt              # JwtAuthenticationFilter, JwtUtil
└── service              # logika biznesowa (do dodania)
```

## Baza danych
- Silnik: MariaDB, baza: `saper`, kodowanie: utf8mb4
- Migracje Liquibase (XML):
    - `001-create-users.xml`
    - `002-create-games.xml`
    - `003-create-scores.xml`
- Changelog master: `src/main/resources/db/changelog/db.changelog-master.xml`

## Ważne decyzje
- `application.properties` NIE jest w repo (dane wrażliwe) — każdy deweloper konfiguruje lokalnie
- `spring.jpa.hibernate.ddl-auto=validate` — schemat zarządzany wyłącznie przez Liquibase
- Pozycje min nigdy nie wychodzą do frontendu podczas trwającej gry
- Logika wygranej/przegranej wyłącznie na backendzie
- Plansza generowana dopiero po pierwszym kliknięciu (bezpieczny start)

## Instrukcja dla AI (początek sesji)
1. Przeczytaj `PROGRESS.md` — sprawdź aktualny etap i ostatni krok
2. Przeczytaj `AI_CONTEXT.md` — ten plik
3. Zapytaj użytkownika o potwierdzenie przed rozpoczęciem nowego etapu
4. Pracuj krok po kroku, czekaj na "OK" przed kolejną czynnością
5. Po ukończeniu etapu: commit → merge do develop → aktualizuj oba pliki MD → push