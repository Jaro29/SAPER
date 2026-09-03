# 🐱 Koci Saper (CatSweeper) – Web MVP

Webowa wersja klasycznego Sapera z **kocim motywem**.  
Logika gry działa w pełni po stronie serwera. Autoryzacja JWT, zapis wyników i globalny ranking.

## ✨ Funkcjonalności

- Rejestracja i logowanie (JWT)
- Trzy poziomy trudności: Łatwy (9×9), Średni (16×16), Trudny (30×16)
- Bezpieczny pierwszy klik – plansza generowana dopiero po pierwszym ruchu
- Lawinowe odkrywanie pól (algorytm BFS)
- Zapis czasu przy wygranej
- Ranking globalny i historia własnych wyników
- Pełny koci motyw wizualny 🐱

## 🛠️ Stack

| Warstwa     | Technologia                                       |
| ----------- | ------------------------------------------------- |
| Backend     | Java 21 + Spring Boot 3.5 + Spring Security + JWT |
| Baza danych | MariaDB + Liquibase                               |
| Frontend    | Angular 19 + TypeScript + Tailwind CSS 4          |

## 🚀 Uruchomienie projektu

### 1. Backend

```bash
# 1. Skonfiguruj application.properties (patrz niżej)
# 2. Uruchom aplikację
./mvnw spring-boot:run
```

**application.properties** (utwórz plik lokalnie – nie wrzucaj do repo!):

```properties
spring.application.name=koci-saper

# === MariaDB ===
spring.datasource.url=jdbc:mariadb://localhost:3306/saper?useUnicode=true&characterEncoding=utf8mb4
spring.datasource.username=root
spring.datasource.password=TWOJE_HASLO
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver

# === JPA + Liquibase ===
spring.jpa.hibernate.ddl-auto=validate
spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.xml

# === JWT ===
jwt.secret=ZMIEŃ_NA_BARDZO_DŁUGIE_I_LOSOWE_HASŁO_2026!
jwt.expiration=86400000   # 24 godziny
```

### 2. Frontend

```bash
cd frontend
npm install
npm start
```

Aplikacja będzie dostępna pod adresem: **http://localhost:4200**

## 📁 Struktura projektu

```
saper/
├── src/                    # Backend Spring Boot
├── frontend/               # Angular 19
├── db/changelog/           # Migracje Liquibase
├── Saper_PRD.md            # Pełna specyfikacja projektu
├── AI_CONTEXT.md           # Kontekst i zasady pracy z AI
├── PROGRESS.md             # Aktualny postęp prac
├── README.md
└── pom.xml
```

## 🎮 Jak grać

1. Zarejestruj się lub zaloguj
2. Wybierz poziom trudności
3. Kliknij dowolne pole – pierwszy ruch jest zawsze bezpieczny
4. Lewy klik = odkryj pole  
   Prawy klik = postaw/zdejmij flagę
5. Wygrywasz, gdy odkryjesz wszystkie pola bez ukrytych kotów

## 🐱 Motyw gry

Cała aplikacja utrzymana jest w zabawnym kocim stylu – ukryte miny to koty bawiące się w chowanego 🐱

## 📋 Status projektu

Aktualny status prac: zobacz [`PROGRESS.md`](PROGRESS.md).

---
