# 💣 Saper Web MVP

Webowa implementacja klasycznej gry Saper z logiką kontrolowaną po stronie serwera.

## Stack

| Warstwa     | Technologia                                       |
| ----------- | ------------------------------------------------- |
| Backend     | Java 21 + Spring Boot 3.5 + Spring Security + JWT |
| Baza danych | MariaDB + Liquibase                               |
| Frontend    | Angular 19 + TypeScript + Tailwind CSS 4          |

## Funkcjonalności

- Rejestracja i logowanie z autoryzacją JWT
- Trzy poziomy trudności: Łatwy (9×9), Średni (16×16), Trudny (30×16)
- Bezpieczny pierwszy klik — plansza generowana po pierwszym kliknięciu
- Lawinowe odkrywanie komórek (BFS)
- Zapis wyników i ranking globalny
- Pozycje min nigdy nie wychodzą do frontendu

## Uruchomienie

### Backend

```bash
# Wymagania: Java 21, MariaDB
# Skonfiguruj src/main/resources/application.properties (patrz niżej)
./mvnw spring-boot:run
```

### application.properties (lokalnie, nie w repo)

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/saper
spring.datasource.username=root
spring.datasource.password=TWOJE_HASLO
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
spring.jpa.hibernate.ddl-auto=validate
spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.xml
jwt.secret=TWOJ_SECRET
jwt.expiration=86400000
```

### Frontend

```bash
cd frontend
npm install
npm start
# Aplikacja dostępna na http://localhost:4200
```

## Struktura projektu

```text
saper/
├── src/                    # Backend Java (Spring Boot)
├── frontend/               # Frontend Angular 19
├── Saper_PRD.md            # Specyfikacja projektu
└── README.md
```

## Autor

[Jaro29](https://github.com/Jaro29)
