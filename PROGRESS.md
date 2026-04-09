## Instrukcja dla AI (początek sesji)
1. Repozytorium: https://github.com/Jaro29/SAPER.git
2. Branch strategy: main → develop → feature/etap-XX-nazwa
3. Po każdym etapie: commit na feature branch → merge do develop → push
4. Aktualizuj PROGRESS.md po każdym etapie
5. application.properties NIE jest w repo (dane wrażliwe) — użytkownik konfiguruje lokalnie

# Saper Web MVP — Progress

## Stack
Java 21 + Spring Boot 3.5 + Spring Security + JWT + MariaDB + TypeScript + Tailwind CSS

## Etapy

- [x] Etap 1: Inicjalizacja projektu Spring Boot
- [x] Etap 2a: Encja User.java
- [x] Etap 2b: Encja Game.java
- [x] Etap 2c: Encja Score.java
- [x] Etap 3: Konfiguracja (MariaDB) + migracje (Liquibase)
- [ ] Etap 4: Autoryzacja JWT 
- [ ] Etap 5: Logika gry (generowanie planszy, BFS)
- [ ] Etap 6: Endpointy REST gry
- [ ] Etap 7: Endpointy wyników i rankingu
- [ ] Etap 8: Frontend (TS + Tailwind)
- [ ] Etap 9: Integracja frontend ↔ backend
- [ ] Etap 10: Testy i weryfikacja MVP

## Ostatni krok
Praca nad Etap 4 — Zmieniono układ pakietów (zaktualizowano w AI_CONTEXT.md). Autoryzacja JWT dodano w security: AuthService, w dto: AuthRequest, AuthResponse, w jwt: JwtAuthenticationFilter, JwtUtil, w config: SecurityConfig, w repository: UserRepository
Dodano odpowiednie zależności do pom.xml.
do application.properties dodano:
jwt.secret=########
jwt.expiration=86400000   # 24 godziny w milisekundach
Trzeba utworzyć gałąź dla etapu 4 w git, porobić commity i wypchnąć.
Dalej trzeba dodać UserDetailsService + AuthController aby ukończyć etap 4.

## Pakiety
- model, repository, service, controller, security (dto, jwt, config)
- Group: com.jaro.saper