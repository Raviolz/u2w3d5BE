# Event Booking API

REST API per la gestione di **eventi e prenotazioni**, sviluppata con Java, Spring Boot e PostgreSQL.

Il progetto nasce come esercizio backend durante il percorso Full Stack Development ed è stato successivamente *
*refactorizzato e ampliato**, completando il CRUD degli eventi, introducendo un sistema di prenotazioni, autorizzazioni
basate su ruolo e ownership, DTO dedicati, validazione degli input e test automatici.

---

## ⚙️ Funzionalità principali

L'applicazione permette di:

- registrare e autenticare utenti tramite JWT;
- distinguere tra utenti `UTENTE` e `ORGANIZZATORE`;
- creare, leggere, modificare ed eliminare eventi;
- limitare modifica ed eliminazione degli eventi al relativo organizzatore;
- prenotare posti per un evento;
- consultare e cancellare le proprie prenotazioni;
- calcolare dinamicamente i posti ancora disponibili;
- impedire prenotazioni duplicate o superiori alla disponibilità;
- validare i payload ricevuti dalle API;
- gestire centralmente gli errori HTTP.

---

## 🛠️ Tecnologie utilizzate

| Tecnologia             | Utilizzo                                 |
|------------------------|------------------------------------------|
| **Java**               | Linguaggio principale                    |
| **Spring Boot**        | Configurazione e avvio dell'applicazione |
| **Spring Web MVC**     | REST API e controller                    |
| **Spring Data JPA**    | Accesso ai dati                          |
| **Hibernate / JPA**    | Mapping delle entità                     |
| **PostgreSQL**         | Database relazionale                     |
| **Spring Security**    | Autenticazione e autorizzazione          |
| **JWT / JJWT**         | Autenticazione stateless                 |
| **BCrypt**             | Hash delle password                      |
| **Jakarta Validation** | Validazione dei DTO                      |
| **JUnit 5**            | Test automatici                          |
| **Lombok**             | Riduzione del boilerplate                |
| **Maven**              | Gestione dipendenze e build              |

---

## 🧱 Architettura

L'applicazione segue una struttura a livelli:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
PostgreSQL
```

I DTO separano inoltre i payload utilizzati dalle API dalle entità persistite nel database.

```text
src/main/java/Raviolz/u2w3d5BE/
├── controllers/
├── entities/
├── exception/
├── payloads/
├── repositories/
├── security/
└── services/
```

---

## 🔐 Autenticazione e autorizzazione

L'autenticazione utilizza **Spring Security e JWT**.

Il flusso principale è:

```text
Registrazione
    ↓
Password codificata con BCrypt
    ↓
Login
    ↓
Verifica credenziali
    ↓
Generazione JWT
    ↓
Authorization: Bearer <token>
    ↓
TokenFilter
    ↓
SecurityContext
```

L'applicazione utilizza una policy **stateless**, quindi non mantiene una sessione server tradizionale.

Sono presenti due ruoli:

| Ruolo           | Permessi principali                                        |
|-----------------|------------------------------------------------------------|
| `UTENTE`        | Consultazione eventi e gestione delle proprie prenotazioni |
| `ORGANIZZATORE` | Creazione e gestione dei propri eventi                     |

Per alcune operazioni vengono applicati due livelli distinti di controllo:

- **ruolo**, tramite Spring Security e `@PreAuthorize`;
- **ownership**, verificando che la risorsa appartenga realmente all'utente autenticato.

Una richiesta senza autenticazione verso una risorsa protetta restituisce `401 Unauthorized`, mentre un utente
autenticato senza i permessi necessari riceve `403 Forbidden`.

---

## 🎟️ Gestione degli eventi

Gli organizzatori possono creare, modificare ed eliminare i propri eventi.

Ogni evento contiene:

- titolo;
- descrizione;
- data;
- luogo;
- numero totale di posti;
- organizzatore.

Le response includono anche il numero di **posti disponibili**, calcolato dinamicamente a partire dalle prenotazioni
esistenti:

```text
posti disponibili =
posti totali - posti già prenotati
```

Il valore non viene quindi duplicato nel database.

Tra le principali regole applicative:

- data dell'evento non precedente a quella corrente;
- almeno un posto disponibile alla creazione;
- controllo dei duplicati sulla combinazione titolo/data/luogo;
- solo il proprietario può modificare o eliminare un evento;
- i posti totali non possono essere ridotti sotto il numero di posti già prenotati;
- un evento con prenotazioni attive non può essere eliminato.

---

## 🎫 Prenotazioni

Un utente può prenotare uno o più posti per un evento.

Il sistema verifica che:

- l'evento esista;
- l'evento non sia già terminato;
- il numero di posti richiesto sia valido;
- siano disponibili abbastanza posti;
- lo stesso utente non abbia già prenotato lo stesso evento.

La doppia prenotazione è inoltre protetta da un **vincolo di unicità a livello database** sulla coppia utente/evento.

L'utente può consultare solamente le proprie prenotazioni e può cancellare esclusivamente quelle di cui è proprietario.

---

## 🌐 Endpoint principali

### Autenticazione

| Metodo | Endpoint             | Descrizione             |
|--------|----------------------|-------------------------|
| `POST` | `/auth/registration` | Registrazione           |
| `POST` | `/auth/login`        | Login e generazione JWT |

### Utenti

| Metodo | Endpoint     | Descrizione                     |
|--------|--------------|---------------------------------|
| `GET`  | `/utenti/me` | Profilo dell'utente autenticato |

### Eventi

| Metodo   | Endpoint       | Accesso                  |
|----------|----------------|--------------------------|
| `GET`    | `/eventi`      | Utente autenticato       |
| `GET`    | `/eventi/{id}` | Utente autenticato       |
| `POST`   | `/eventi`      | `ORGANIZZATORE`          |
| `PUT`    | `/eventi/{id}` | Proprietario dell'evento |
| `DELETE` | `/eventi/{id}` | Proprietario dell'evento |

### Prenotazioni

| Metodo   | Endpoint                          | Accesso                         |
|----------|-----------------------------------|---------------------------------|
| `POST`   | `/eventi/{eventoId}/prenotazioni` | `UTENTE`                        |
| `GET`    | `/prenotazioni/me`                | `UTENTE`                        |
| `DELETE` | `/prenotazioni/{id}`              | Proprietario della prenotazione |

---

## ✅ Validazione e gestione degli errori

I payload vengono validati tramite **Jakarta Validation**, utilizzando annotazioni come:

```java
@NotBlank
@NotNull
@Email
@Size
@Min
@FutureOrPresent
```

La gestione centralizzata delle eccezioni permette di restituire status HTTP coerenti, tra cui:

```text
400 Bad Request
401 Unauthorized
403 Forbidden
404 Not Found
409 Conflict
500 Internal Server Error
```

---

## 🧪 Test

Il progetto include test automatici sviluppati con **JUnit 5**.

I test coprono alcuni comportamenti del dominio e la validazione dei payload, tra cui:

- aggiornamento dei dati di un evento;
- utilizzo dell'email come username per Spring Security;
- validazione della data degli eventi;
- validazione del numero di posti;
- validazione delle prenotazioni;
- validazione degli indirizzi email;
- casi validi e non validi tramite test parametrizzati.

Per la Bean Validation viene utilizzato il `Validator` di Jakarta, verificando direttamente le constraint definite sui
DTO.

---

## ▶️ Avvio del progetto

### Requisiti

- Java
- Maven
- PostgreSQL

Creare un database PostgreSQL e aggiungere nella root del progetto un file:

```text
env.properties
```

con le configurazioni necessarie:

```properties
PORT=8080
DB_PORT=5432
DB_NAME=nome_database
DB_USERNAME=postgres
DB_PASSWORD=password
jwt_secret=your_jwt_secret
```

La chiave JWT deve essere sufficientemente lunga per la firma del token.

Avviare quindi l'applicazione:

```bash
./mvnw spring-boot:run
```

Su Windows:

```bash
mvnw.cmd spring-boot:run
```

Le tabelle vengono gestite tramite Hibernate/JPA.

---

## 📌 Obiettivo del progetto

Il progetto è stato utilizzato per consolidare alcuni dei concetti principali dello sviluppo backend con Spring Boot:

- progettazione di REST API;
- separazione Controller / Service / Repository;
- persistenza relazionale con JPA;
- DTO di request e response;
- autenticazione JWT;
- autorizzazione per ruolo e proprietà della risorsa;
- business logic applicativa;
- validazione e gestione degli errori;
- test automatici con JUnit.