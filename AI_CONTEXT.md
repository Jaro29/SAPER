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
- Frontend: Angular 19 + TypeScript + Tailwind CSS 4 (etap 8+)
- IDE: IntelliJ IDEA, System: CachyOS Linux

## Struktura pakietów i encje

```text
com.jaro.saper
├── controller
│   ├── AuthController
│   ├── GameController
│   └── ScoreController
├── dto
│   ├── CellDto
│   ├── GameMapper
│   ├── GameRequest
│   ├── GameResponse
│   ├── MoveRequest
│   ├── RankingEntry
│   ├── ScoreRequest
│   └── ScoreResponse
├── model
│   ├── Cell
│   ├── Game
│   ├── Score
│   └── User
├── repository
│   ├── GameRepository
│   ├── ScoreRepository
│   └── UserRepository
├── security
│   ├── config
│   │   └── SecurityConfig
│   ├── dto
│   │   ├── AuthRequest
│   │   └── AuthResponse
│   ├── jwt
│   │   ├── JwtAuthenticationFilter
│   │   └── JwtUtil
│   ├── AuthService
│   └── CustomUserDetailsService
├── service
│   ├── BoardSerializer
│   ├── BoardService
│   ├── GameService
│   └── ScoreService
└── SaperApplication

frontend/src/app/
├── core/
│   ├── interceptors/
│   │   └── jwt.interceptor.ts              
│   ├── models/
│   │   ├── auth.model.ts                   
│   │   ├── game.model.ts                   
│   │   └── score.model.ts                  
│   └── services/
│       ├── auth.service.ts                 
│       ├── game.service.ts                 
│       └── score.service.ts                
├── features/
│   ├── auth/
│   │   ├── auth.component.ts               
│   │   └── auth.component.html             
│   ├── lobby/
│   │   ├── lobby.component.ts              
│   │   ├── lobby.component.html            
│   │   └── lobby.component.css             
│   ├── game/
│   │   ├── game.component.ts               
│   │   ├── game.component.html             
│   │   └── game.component.css              
│   └── ranking/
│       ├── ranking.component.ts            
│       ├── ranking.component.html          
│       └── ranking.component.css           
└── ...
```

## Baza danych

- Silnik: MariaDB, baza: `saper`, kodowanie: utf8mb4
- Migracje Liquibase (XML): 001-create-users, 002-create-games, 003-create-scores
- Changelog master: `src/main/resources/db/changelog/db.changelog-master.xml`
- `spring.jpa.hibernate.ddl-auto=validate` — schemat tylko przez Liquibase

## Motyw wizualny
- **Wszędzie koty** — projekt ma koci charakter wizualny
- Miny = ukryte koty bawiące się w chowanego 🐱
- Emoji, ikony, komunikaty — wszystko nawiązuje do kotów
- Nazwa gry może ewoluować np. "Koci Saper" lub "CatSweeper"

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
