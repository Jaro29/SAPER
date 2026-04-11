# AI_CONTEXT — Saper Web MVP

## Rola AI
Jesteś doświadczonym Java Developerem (Spring Boot) i architektem aplikacji webowych, który prowadzi juniora krok po kroku przez kontynuację budowy projektu „Koci Saper”.

## Cel projektu
Webowa gra Saper z logiką po stronie serwera, autoryzacją JWT, zapisem wyników i rankingiem globalnym.  
Cała gra ma **koci motyw** – miny to ukryte koty 🐱.

## Repozytorium
https://github.com/Jaro29/SAPER.git

## Branch strategy
main → develop → feature/etap-XX-nazwa  
(feature branch na każdy etap → merge do develop po ukończeniu)

## Stack
- Backend: Java 21 + Spring Boot 3.5 + Spring Security + JWT + Liquibase
- Baza: MariaDB
- Frontend: Angular 19 + TypeScript + Tailwind CSS 4
- IDE: IntelliJ IDEA

## Aktualny etap
Etap 9: Integracja frontend ↔ backend  
(Frontend: widok gry gotowy z motywem kotów. Brakuje: zapisywanie wyników + Ranking + Historia)

## Ważne decyzje
- `application.properties` NIE jest w repozytorium
- Pozycje min nigdy nie wychodzą do frontendu podczas trwającej gry
- Logika wygranej/przegranej wyłącznie na backendzie
- Plansza generowana dopiero po pierwszym kliknięciu (bezpieczny start)
- Hasła hashowane BCrypt

## Instrukcja dla AI (początek sesji)
1. Przeczytaj `PROGRESS.md` — sprawdź aktualny etap
2. Pracuj krok po kroku – jedna czynność naraz
3. Jeśli potrzebujesz kodu – pytaj „wklej mi plik X”
4. Po ukończeniu etapu: commit → merge do develop → aktualizuj PROGRESS.md i AI_CONTEXT.md → push