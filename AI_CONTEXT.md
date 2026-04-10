# AI_CONTEXT — Saper Web MVP

## Rola AI w projekcie
Jesteś doświadczonym Java Developerem (Spring Boot) i architektem aplikacji webowych,
który prowadzi juniora krok po kroku przez kontynuację budowy tego projektu.
Poziom użytkownika: Junior (Java, Spring Boot, Git).

## Cel projektu
Webowa gra Saper z logiką po stronie serwera. Autoryzacja JWT, zapis wyników, ranking globalny.
PRD: `Saper_PRD.md` — zawiera pełne API, format JSON, algorytm BFS, kryteria MVP.

## Repozytorium
https://github.com/Jaro29/SAPER.git

## Branch strategy
main → develop → feature/etap-XX-nazwa
- feature branch per etap → merge do develop po ukończeniu → main po MVP

## Stack
- Backend: Java 21 + Spring Boot 3.5.x + Spring Security + JWT + Liquibase
- Baza: MariaDB (nie MySQL!) — sterownik `mariadb-java-client`
- Frontend: TypeScript + Tailwind CSS (etap 8+)
- IDE: IntelliJ IDEA, System: CachyOS Linux

## Struktura pakietów i encje
```
com.jaro.saper
├── model
│   ├── User.java        → id, login, passwordHash, createdAt
│   ├── Game.java        → id, user, status(enum), difficulty(enum), boardState(JSON), createdAt, finishedAt
│   │                       Enums zagnieżdżone: Game.GameStatus, Game.Difficulty
│   ├── Score.java       → id, user, game(UNIQUE), difficulty, timeSeconds, playedAt
│   └── Cell.java        → value(-1=mina, 0-8), state(enum: HIDDEN/REVEALED/FLAGGED)
├── repository
│   ├── UserRepository   → findByLogin, existsByLogin
│   └── GameRepository   → findByUser
├── controller
│   └── AuthController   → POST /api/auth/register, POST /api/auth/login
├── security
│   ├── AuthService      → register (BCrypt), login (AuthenticationManager)
│   ├── CustomUserDetailsService
│   ├── config/SecurityConfig    → JWT stateless, BCryptPasswordEncoder
│   ├── dto/AuthRequest, AuthResponse
│   └── jwt/JwtUtil, JwtAuthenticationFilter
└── service
    ├── BoardService     → generateBoard, reveal (BFS), isWon
    ├── BoardSerializer  → serialize/deserialize Cell[][] ↔ JSON
    └── GameService      → createGame, reveal, flag, getGameForUser
```
## Baza danych
- Silnik: MariaDB, baza: `saper`, kodowanie: utf8mb4
- Migracje Liquibase (XML): 001-create-users, 002-create-games, 003-create-scores
- Changelog master: `src/main/resources/db/changelog/db.changelog-master.xml`
- `spring.jpa.hibernate.ddl-auto=validate` — schemat tylko przez Liquibase

## Ważne decyzje
- `application.properties` NIE jest w repo — każdy deweloper konfiguruje lokalnie
- Pozycje min nigdy nie wychodzą do frontendu podczas trwającej gry
- Logika wygranej/przegranej wyłącznie na backendzie
- Plansza generowana dopiero po pierwszym kliknięciu (bezpieczny start)
- Hasła hashowane BCrypt (cost factor 10)

## Instrukcja dla AI (początek sesji)
1. Przeczytaj `PROGRESS.md` — aktualny etap i ostatni krok
2. Przeczytaj `AI_CONTEXT.md` — ten plik
3. Jeśli potrzebujesz zobaczyć istniejący kod — powiedz "wklej mi plik X"
4. Pracuj krok po kroku, czekaj na "OK" przed kolejną czynnością
5. Po ukończeniu etapu: commit → merge do develop → zaktualizuj oba pliki MD → push