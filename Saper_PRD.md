# PRD: Saper Web — MVP
**Wersja:** 1.1
**Status:** Draft
**Autor:** [imię/nick]
**Data:** [YYYY-MM-DD]

---

## 0. Metadane dokumentu

| Pole | Wartość |
|---|---|
| Projekt | Saper Web MVP |
| Repozytorium | [URL] |
| Stack | Java + Spring Boot / Angular 19 + Tailwind / MariaDB |
| Środowisko docelowe | Przeglądarka (desktop-first) |

> **Jak używać tego dokumentu:**
> Każda sekcja zawiera komentarz `[SZABLON]` tam, gdzie kolejny projekt powinien uzupełnić własne dane. Sekcje bez tego znacznika są specyficzne dla Sapera i powinny być przepisane od nowa.

---

## 1. Wprowadzenie

### 1.1 Cel dokumentu
Dokument definiuje pełny zakres wymagań funkcjonalnych i niefunkcjonalnych dla MVP webowej gry Saper. Stanowi jedyne źródło prawdy dla zespołu developerskiego i jest podstawą do testów akceptacyjnych.

### 1.2 Zakres projektu
System umożliwi użytkownikowi:
- założenie konta i logowanie,
- rozegranie partii Sapera w przeglądarce z logiką kontrolowaną po stronie serwera,
- automatyczny zapis wyniku po zakończeniu gry,
- przeglądanie własnej historii wyników oraz globalnego rankingu per poziom trudności.

### 1.3 Poza zakresem MVP

| Wykluczone | Powód |
|---|---|
| Swobodny wybór parametrów planszy | Zastąpione presetami poziomów trudności |
| Multiplayer w czasie rzeczywistym | Zbyt duża złożoność |
| Zaawansowane animacje i efekty | Brak wartości dla MVP |
| Aplikacja natywna iOS/Android | Odrębny projekt |
| Wznawianie przerwanej gry | Uproszczono: odświeżenie = anulowanie gry |
| Funkcje społecznościowe (czat, zaproszenia) | Poza celem MVP |

### 1.4 Słowniczek pojęć

| Pojęcie | Definicja |
|---|---|
| **Plansza** | Siatka `rows × cols` komórek generowana przez backend po pierwszym kliknięciu. |
| **Komórka** | Pojedyncze pole planszy. Może być w stanie: `hidden`, `revealed`, `flagged`. |
| **Mina** | Komórka z wartością `-1`. Kliknięcie jej kończy grę przegraną. |
| **Wartość komórki** | Liczba `0–8` oznaczająca liczbę min w sąsiednich komórkach. `0` = brak sąsiadów-min. |
| **Lawina** | Automatyczne odkrywanie sąsiadujących komórek o wartości `0`, uruchamiane algorytmem BFS po odkryciu komórki `0`. |
| **Bezpieczny pierwszy klik** | Gwarancja, że pierwsza kliknięta komórka i jej bezpośredni sąsiedzi (max 9 pól) nigdy nie są miną. |
| **Flaga** | Znacznik stawiany przez użytkownika na `hidden` komórce. Nie chroni przed kliknięciem. |
| **Sesja gry** | Obiekt po stronie serwera reprezentujący pojedynczą partię: stan planszy, status, czasy. |
| **Timer** | Czas gry mierzony po stronie frontendu od pierwszego kliknięcia do zakończenia gry. |
| **Wynik** | Czas ukończenia wygranej gry (w sekundach) zapisany w bazie po zakończeniu sesji. |
| **Poziom trudności** | Jeden z trzech presetów: `EASY`, `MEDIUM`, `HARD` — określa rozmiar planszy i liczbę min. |
| **Ranking** | Lista najlepszych wyników (najkrótszy czas) dla konkretnego poziomu trudności. |

---

## 2. Użytkownicy i przypadki użycia

### 2.1 Profil użytkownika
`[SZABLON — opisz docelowego użytkownika produktu]`

Docelowy użytkownik to gracz chcący rozegrać klasyczną partię Sapera online, śledzić swoje czasy i rywalizować z innymi w rankingu. Nie są wymagane żadne specjalne umiejętności techniczne.

### 2.2 Główny user flow
```
[Rejestracja / Logowanie]
        ↓
     [Lobby]
  Wybór poziomu trudności
  (Łatwy / Średni / Trudny)
        ↓
   [Rozgrywka]
  Kliknięcie startuje timer
  Backend generuje planszę
        ↓
  [Wygrana lub Przegrana]
  Wynik zapisany automatycznie
        ↓
[Historia wyników / Ranking]
```

### 2.3 Przypadki brzegowe — decyzje produktowe

| Sytuacja | Zachowanie systemu |
|---|---|
| Odświeżenie strony w trakcie gry | Gra zostaje oznaczona jako `ABANDONED`, wynik nie jest zapisywany |
| Wygaśnięcie tokenu JWT w trakcie gry | Następne żądanie API zwraca `401`; frontend przekierowuje na logowanie |
| Dwie otwarte zakładki z tą samą grą | Każde żądanie operuje na tym samym `gameId`; last-write-wins |
| Próba ruchu w zakończonej grze | Backend zwraca `409 Conflict` |
| Próba postawienia flagi na odkrytej komórce | Backend zwraca `422 Unprocessable Entity` |

---

## 3. Wymagania funkcjonalne

### 3.1 Autoryzacja

- System umożliwia rejestrację nowego użytkownika podając login i hasło.
- System umożliwia logowanie i zwraca token JWT.
- System identyfikuje użytkownika na podstawie tokenu JWT w nagłówku `Authorization: Bearer <token>`.
- Wszystkie endpointy poza `/api/auth/*` wymagają ważnego tokenu.

**Reguły walidacji danych rejestracji:**

| Pole | Reguła |
|---|---|
| Login | 3–30 znaków, tylko `[a-zA-Z0-9_-]`, unikalne, case-insensitive |
| Hasło | Min. 8 znaków, co najmniej 1 cyfra |
| Potwierdzenie hasła | Musi być identyczne z hasłem (walidacja frontendowa) |

### 3.2 Rozgrywka

- Użytkownik wybiera poziom trudności przed rozpoczęciem gry spośród trzech presetów:

| Poziom | `difficulty` | Wiersze | Kolumny | Miny |
|---|---|---|---|---|
| Łatwy | `EASY` | 9 | 9 | 10 |
| Średni | `MEDIUM` | 16 | 16 | 40 |
| Trudny | `HARD` | 30 | 16 | 99 |

- Nowa sesja gry jest tworzona na backendzie po wyborze poziomu.
- Plansza **nie jest generowana** w momencie tworzenia sesji — generowana jest dopiero po pierwszym kliknięciu (gwarancja bezpiecznego startu).
- Timer startuje na frontendzie w momencie pierwszego kliknięcia.
- Użytkownik może odkrywać komórki (`reveal`) i stawiać/zdejmować flagi (`flag`).
- Odkrycie komórki o wartości `0` uruchamia lawinowe odkrywanie sąsiadów (algorytm BFS po stronie backendu).
- Gra kończy się **wygraną**, gdy wszystkie komórki bez min mają stan `revealed`.
- Gra kończy się **przegraną**, gdy użytkownik odkryje komórkę z miną.
- Po zakończeniu gry backend ujawnia pełną planszę z pozycjami wszystkich min.

### 3.3 Wyniki i ranking

- Po zakończeniu gry wygraną frontend wysyła zmierzony czas do backendu; backend zapisuje wynik.
- Wynik zawiera: `userId`, `difficulty`, `time_seconds`.
- Użytkownik może przeglądać własną historię wszystkich zakończonych gier (wygrane i przegrane).
- Ranking globalny jest **osobny dla każdego poziomu trudności** (`EASY` / `MEDIUM` / `HARD`) i sortuje po `time_seconds` rosnąco.
- W rankingu dla danego poziomu wyświetlanych jest top 10 wyników.

---

## 4. Wymagania niefunkcjonalne

### 4.1 Bezpieczeństwo

- Hasła przechowywane wyłącznie jako hash BCrypt (min. cost factor 10).
- Stan min **nigdy** nie jest wysyłany do frontendu podczas trwającej gry — backend zwraca tylko widok komórek dostępny dla gracza.
- Logika wygranej, przegranej i walidacji ruchu realizowana **wyłącznie na backendzie**.
- Token JWT: czas życia access tokenu — 24h. `[SZABLON — dostosuj do polityki bezpieczeństwa projektu]`
- Wszystkie requesty przez HTTPS w środowisku produkcyjnym.

### 4.2 Wydajność

- Odpowiedź backendu na ruch gracza (`reveal`, `flag`): p95 ≤ 300ms.
- Aplikacja obsługuje co najmniej 20 równoczesnych sesji gry bez degradacji.
- Lawinowe odkrywanie (BFS) musi zakończyć się w czasie włączonym w powyższy limit 300ms nawet dla planszy 30×16.

### 4.3 Interfejs i ergonomia

- Aplikacja jest desktop-first; działa poprawnie na ekranach ≥ 1024px szerokości.
- Responsywna na urządzeniach mobilnych (użyteczna, nie zoptymalizowana).
- Kluczowe operacje (start gry, oznaczenie komórki) dostępne w maksymalnie 3 kliknięciach od ekranu lobby.
- Formularze posiadają walidację po stronie klienta **i** serwera.

### 4.4 Niezawodność danych

- System zapobiega zapisaniu zduplikowanego wyniku dla tej samej sesji gry.
- Wynik zapisywany jest tylko dla gier ze statusem `WON`.

### 4.5 Testowalność

- Aplikacja wspiera tryb testowy z możliwością uruchomienia na seed-owanych danych.
- Logika generowania planszy, BFS i warunki zakończenia gry projektowane z myślą o testach jednostkowych (czyste funkcje bez zależności od HTTP).

---

## 5. Architektura rozwiązania

### 5.1 Podział odpowiedzialności

| Warstwa | Odpowiedzialność |
|---|---|
| **Backend** | Generowanie planszy, rozmieszczenie min, BFS, walidacja ruchów, wykrywanie wygranej/przegranej, zapis wyników, autoryzacja |
| **Frontend** | Renderowanie planszy, obsługa kliknięć, timer (pomiar czasu), komunikacja z API, routing widoków |
| **Baza danych** | Persystencja użytkowników, sesji gier, wyników |

### 5.2 Stack technologiczny

| Warstwa | Technologia                              | Uzasadnienie |
|---|------------------------------------------|---|
| Backend | Java 21 + Spring Boot 3.x                | `[SZABLON]` |
| Bezpieczeństwo | Spring Security + JWT + BCrypt           | `[SZABLON]` |
| Frontend | Angular 19 + TypeScript + Tailwind CSS 4 | Kompletny framework z wbudowanym routingiem i klientem HTTP, szybki build (Vite) |
| Baza danych | MariaDB | `[SZABLON]` |
| Repozytorium | GitHub | `[SZABLON]` |

---

## 6. Model danych

### 6.1 Tabela `users`

| Kolumna | Typ | Ograniczenia |
|---|---|---|
| `id` | BIGINT | PK, AUTO_INCREMENT |
| `login` | VARCHAR(30) | UNIQUE, NOT NULL |
| `password_hash` | VARCHAR(255) | NOT NULL |
| `created_at` | DATETIME | NOT NULL |

### 6.2 Tabela `games`

| Kolumna | Typ | Ograniczenia |
|---|---|---|
| `id` | BIGINT | PK, AUTO_INCREMENT |
| `user_id` | BIGINT | FK → users.id, NOT NULL |
| `status` | ENUM | `IN_PROGRESS`, `WON`, `LOST`, `ABANDONED` |
| `difficulty` | ENUM | `EASY`, `MEDIUM`, `HARD`, NOT NULL |
| `board_state` | JSON | NOT NULL — patrz §6.4 |
| `created_at` | DATETIME | NOT NULL |
| `finished_at` | DATETIME | NULL |

### 6.3 Tabela `scores`

| Kolumna | Typ | Ograniczenia |
|---|---|---|
| `id` | BIGINT | PK, AUTO_INCREMENT |
| `game_id` | BIGINT | FK → games.id, UNIQUE, NOT NULL |
| `user_id` | BIGINT | FK → users.id, NOT NULL |
| `difficulty` | ENUM | `EASY`, `MEDIUM`, `HARD`, NOT NULL |
| `time_seconds` | INT | NOT NULL |
| `created_at` | DATETIME | NOT NULL |

> `UNIQUE` na `game_id` zapobiega duplikatom wyników dla tej samej sesji.

### 6.4 Schemat `board_state` (JSON)
```json
{
  "cells": [
    {
      "row": 0,
      "col": 0,
      "value": -1,
      "state": "hidden"
    }
  ]
}
```

| Pole | Typ | Opis |
|---|---|---|
| `row` | int | Indeks wiersza (0-based) |
| `col` | int | Indeks kolumny (0-based) |
| `value` | int | `-1` = mina, `0–8` = liczba sąsiednich min |
| `state` | string | `hidden` \| `revealed` \| `flagged` |

---

## 7. API

### 7.1 Konwencje

- Wszystkie requesty i response body w formacie JSON.
- Nagłówek autoryzacji: `Authorization: Bearer <token>` (wymagany wszędzie poza `/api/auth/*`).
- Błędy zwracane w ujednoliconym formacie:
```json
{
  "status": 400,
  "error": "BAD_REQUEST",
  "message": "Opis błędu czytelny dla developera"
}
```

### 7.2 Kody błędów HTTP

| Kod | Znaczenie | Przykład |
|---|---|---|
| `400` | Nieprawidłowe dane wejściowe | Nieznana wartość `difficulty` |
| `401` | Brak lub nieważny token | Wygasły JWT |
| `404` | Zasób nie istnieje | Nieznane `gameId` |
| `409` | Konflikt stanu | Ruch w zakończonej grze |
| `422` | Semantycznie niepoprawny ruch | Flaga na odkrytej komórce |

### 7.3 Endpointy autoryzacji

#### `POST /api/auth/register`
```json
// Request
{ "login": "jarek99", "password": "haslo123" }

// Response 201
{ "message": "Konto utworzone pomyślnie" }

// Błędy: 400 (walidacja), 409 (login zajęty)
```

#### `POST /api/auth/login`
```json
// Request
{ "login": "jarek99", "password": "haslo123" }

// Response 200
{ "token": "eyJ..." }

// Błędy: 401 (złe dane)
```

### 7.4 Endpointy użytkownika

#### `GET /api/users/me`
```json
// Response 200
{ "id": 1, "login": "jarek99", "createdAt": "2025-01-15T10:00:00Z" }
```

### 7.5 Endpointy gry

#### `POST /api/games`
```json
// Request
{ "difficulty": "EASY" }
// difficulty: "EASY" | "MEDIUM" | "HARD"

// Response 201
{
  "gameId": 42,
  "status": "IN_PROGRESS",
  "difficulty": "EASY",
  "rows": 9,
  "cols": 9,
  "mines": 10
}

// Błędy: 400 (nieznana wartość difficulty)
```

#### `GET /api/games/{gameId}`
```json
// Response 200 — widok gracza (bez pozycji min podczas trwającej gry)
{
  "gameId": 42,
  "status": "IN_PROGRESS",
  "difficulty": "EASY",
  "rows": 9,
  "cols": 9,
  "mines": 10,
  "flagsPlaced": 3,
  "cells": [
    { "row": 0, "col": 0, "state": "revealed", "value": 2    },
    { "row": 0, "col": 1, "state": "hidden",   "value": null },
    { "row": 0, "col": 2, "state": "flagged",  "value": null }
  ]
}
```
> `value` jest `null` dla komórek `hidden` i `flagged` — backend nie ujawnia ich zawartości.
> Po zakończeniu gry (`WON`/`LOST`) backend zwraca `value` dla wszystkich komórek.

#### `POST /api/games/{gameId}/reveal`
```json
// Request
{ "row": 3, "col": 5 }

// Response 200
{
  "gameId": 42,
  "status": "IN_PROGRESS",
  "cells": [ /* zaktualizowany widok planszy jak w GET */ ]
}

// Błędy: 400 (koordynaty poza planszą), 409 (gra zakończona), 422 (komórka już odkryta)
```

#### `POST /api/games/{gameId}/flag`
```json
// Request
{ "row": 3, "col": 5 }

// Response 200
{
  "gameId": 42,
  "flagsPlaced": 4,
  "cells": [ /* zaktualizowany widok planszy */ ]
}

// Błędy: 409 (gra zakończona), 422 (komórka odkryta)
```

### 7.6 Endpointy wyników

#### `POST /api/scores`
```json
// Request (wysyłany przez frontend po wygranej, z timerem zmierzonym po stronie klienta)
{ "gameId": 42, "time_seconds": 87 }

// Response 201
{ "scoreId": 15, "time_seconds": 87 }

// Błędy: 400 (time_seconds ≤ 0), 409 (wynik już istnieje dla tego gameId), 422 (gra nie ma statusu WON)
```

#### `GET /api/scores/personal`
```json
// Response 200
[
  {
    "gameId": 42,
    "difficulty": "EASY",
    "status": "WON",
    "time_seconds": 87,
    "playedAt": "2025-01-15T12:30:00Z"
  }
]
```

#### `GET /api/scores/global?difficulty=EASY`
```json
// Response 200
[
  { "rank": 1, "login": "jarek99", "time_seconds": 45 },
  { "rank": 2, "login": "gracz22", "time_seconds": 61 }
]

// Parametr query `difficulty` jest wymagany: "EASY" | "MEDIUM" | "HARD". Brak → 400.
```

---

## 8. Logika gry — specyfikacja techniczna

### 8.1 Generowanie planszy (po pierwszym kliknięciu)

1. Zdefiniuj strefę bezpieczną: kliknięta komórka `(row, col)` oraz jej wszyscy sąsiedzi (max 9 pól, mniej na krawędzi planszy).
2. Rozmieszaj `mines` min losowo w komórkach **poza strefą bezpieczną**.
3. Dla każdej komórki oblicz `value` = liczba min w 8 sąsiadach.
4. Zapisz gotową planszę w `board_state`.
5. Wykonaj odkrycie pierwszej komórki (uruchom BFS jeśli `value == 0`).

### 8.2 Algorytm lawinowego odkrywania (BFS)
```
wejście: komórka startowa (row, col)
1. Dodaj (row, col) do kolejki Q i oznacz jako revealed
2. Dopóki Q nie jest pusta:
   a. Pobierz komórkę C z Q
   b. Jeśli C.value == 0:
      - Dla każdego sąsiada S komórki C:
        - Jeśli S.state == "hidden": oznacz S jako revealed, dodaj do Q
3. Zwróć zaktualizowaną planszę
```

> Flagi **nie blokują** lawiny — sąsiad z flagą jest odkrywany i flaga jest zdejmowana.
> `[SZABLON — zdecyduj i zapisz jawnie zachowanie flag w lawinie dla swojego projektu]`

### 8.3 Warunki zakończenia gry

| Warunek | Status gry | Akcja |
|---|---|---|
| Odkryto komórkę z `value == -1` | `LOST` | Backend ujawnia pełną planszę; wynik nie jest zapisywany |
| Liczba `revealed` komórek == `(rows × cols) - mines` | `WON` | Backend ustawia `finished_at`; frontend wysyła wynik do `POST /api/scores` |
| Odświeżenie / zamknięcie przez użytkownika | `ABANDONED` | Ustawiany przy następnym żądaniu na tę sesję lub przez job czyszczący stare sesje `IN_PROGRESS` |

---

## 9. Widoki aplikacji

### 9.1 Ekran logowania / rejestracji
- Dwa tryby przełączane zakładką lub linkiem: *Logowanie* / *Rejestracja*.
- Walidacja inline (bez przeładowania strony).
- Po zalogowaniu: przekierowanie do Lobby.

### 9.2 Lobby
- Trzy przyciski wyboru poziomu: **Łatwy**, **Średni**, **Trudny** z widocznym opisem parametrów (np. „9×9 / 10 min").
- Kliknięcie poziomu tworzy sesję i przechodzi do widoku rozgrywki.
- Link do historii wyników i rankingu.

### 9.3 Widok rozgrywki
- Plansza renderowana jako siatka.
- Timer widoczny w nagłówku — startuje przy pierwszym kliknięciu.
- Licznik pozostałych flag (`mines - flagsPlaced`).
- Lewy klik = `reveal`, prawy klik = `flag`.
- Po zakończeniu gry: modal z wynikiem (czas lub „Przegrana") i przyciskami „Nowa gra" / „Wróć do lobby".

### 9.4 Historia i ranking
- Zakładka **Moje wyniki**: tabela z kolumnami `Data`, `Poziom`, `Status`, `Czas`.
- Zakładka **Ranking**: wybór poziomu trudności (Łatwy / Średni / Trudny) + tabela top 10.

---

## 10. Kryteria ukończenia MVP

MVP jest ukończone, gdy spełnione są **wszystkie** poniższe kryteria:

### 10.1 Funkcjonalne (testowalne manualnie)

- [ ] Użytkownik może zarejestrować konto i zalogować się.
- [ ] Użytkownik może wybrać poziom trudności spośród trzech presetów.
- [ ] Pierwszy klik nigdy nie trafia na minę ani jej sąsiada.
- [ ] Lawinowe odkrywanie działa poprawnie dla komórek z wartością `0`.
- [ ] Timer startuje przy pierwszym kliknięciu i zatrzymuje się przy zakończeniu gry.
- [ ] Wygrana jest wykrywana poprawnie i wynik jest zapisywany.
- [ ] Przegrana jest wykrywana poprawnie; wynik nie jest zapisywany.
- [ ] Historia wyników użytkownika jest dostępna po zalogowaniu.
- [ ] Ranking globalny wyświetla top 10 osobno dla każdego poziomu trudności.
- [ ] Odświeżenie strony w trakcie gry anuluje grę i nie zapisuje wyniku.

### 10.2 Niefunkcjonalne (weryfikowalne)

- [ ] Pozycje min nie są widoczne w odpowiedziach API podczas trwającej gry (weryfikacja DevTools).
- [ ] Ruch w zakończonej grze zwraca `409`.
- [ ] Próba rejestracji z zajętym loginem zwraca `409`.
- [ ] Żądanie do `GET /api/scores/global` bez parametru `difficulty` zwraca `400`.

---

## 11. Otwarte decyzje — po MVP
`[SZABLON — wypełnij przed kolejną iteracją]`

| Temat | Opcje do rozważenia |
|---|---|
| Swobodny wybór parametrów planszy | Własne `rows / cols / mines` poza presetami |
| Wznawianie gry | `GET /api/games/{id}` przywraca stan po odświeżeniu |
| Obsługa dotykowa | Long-press jako prawy klik (flaga) na mobile |
| Rozbudowany UI | Animacje odkrywania, efekt eksplozji przy przegranej |
| Refresh token | Rotacja tokenów zamiast długiego access tokenu |
