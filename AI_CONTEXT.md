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