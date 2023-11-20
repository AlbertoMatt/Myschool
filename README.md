## Credenziali di Accesso

- **Username:** admin
- **Password:** admin

## API

L'insieme delle API è contenuto nel file `MySchool.postman_collection.json` da importare su Postman. Utilizzando la richiesta "get token" presente nella root della collection, è possibile ottenere un token di autenticazione, il quale viene salvato nella collectionVariable `token`.

### Elenco delle Classi (API Pubblica)

- **Endpoint:** `/api/classes`
- **Metodo:** GET
- **Descrizione:** Restituisce l'elenco delle classi della scuola.

### Altre API (Richiedono Autenticazione)

Le altre API, come quelle per gestire gli alunni e i compiti in classe, richiedono l'autenticazione tramite l'inserimento della variabile `token` nell'header della richiesta, che si può generare facendo la chiamata "get token".

## Configurazione del Database

Si deve modificare il file `src/main/resources/config/application-dev.yml` per configurare l'accesso al database. Si deve modificare nel datasource il nome del database, lo username e la password di connessione allo schema del database, attraverso i campi `spring.datasource.url`, `spring.datasource.username` e `spring.datasource.password`. Inoltre si deve modificare il file `pom.xml` nei campi `liquibase-plugin.url`, `liquibase-plugin.username` e `liquibase-plugin.password`.

## Avvio dell'Applicazione

Per avviare l'applicazione si deve eseguire il seguente comando da terminale nella root del progetto:

```bash
./mvnw
```

## Modifiche Effettuate per Punto

### Punto 1

- Modifiche al file `jhipster_jdl.jdl`:
  - Aggiunta della nuova entity `AlunnoCompito`.
  - Modifica delle entity esistenti.
- Eliminazione dei changelog modificati delle entity `Alunno` e `CompitoInClasse`.

- Esecuzione del comando `./mvnw liquibase:diff`.

- Modifica del file `master.xml`:

  - Inserimento del changelog creato dal comando precedente.

- Modifica della classe `src/main/java/com/mycompany/myapp/web/rest/CompitoInClasseResource.java`:
  - Creazione dell'API PATCH con endpoint "api/{id}/valorizza-data-restituzione".

### Punto 2

- Modifica della classe `src/main/java/com/mycompany/myapp/web/rest/CompitoInClasseResource.java`:
  - Creazione dell'API GET con endpoint "api/{id}/alunni-risultato-superiore?valoreMinimo={valoreMinimo}".

### Punto 3

- Creazione della cartella `src/main/java/com/mycompany/myapp/batch`.
- Inserimento della classe `StampaConteggioCompitiBatch.java` nella cartella sopra indicata.

  - Contiene un metodo annotato con `@Scheduled(cron = "0 0 12 * * ?")` che stampa una riga di log con il numero dei compiti eseguiti 7 giorni prima alle 12 di ogni giorno.

- Annotazione della classe `src/main/java/com/mycompany/myapp/MyschoolApp.java` con `@EnableScheduling`.

- Aggiunta della dipendenza nel file `pom.xml`:
  ```xml
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-batch</artifactId>
  </dependency>
  ```

### Punto 4

- Modifiche al file `jhipster_jdl.jdl`:
  - Aggiunta del campo `mediaVoti` di tipo `Double` all'entità `Alunno`.
- Eliminazione dei changelog modificati delle varie entity.

- Esecuzione del comando `./mvnw liquibase:diff`.

- Modifica del file `master.xml`:

  - Inserimento del changelog creato dal comando precedente.

- Creazione della classe `CalcolaMediaVotiBatch.java` nella cartella `src/main/java/com/mycompany/myapp/batch`.
  - Contiene un metodo annotato con `@Scheduled(cron = "0 0 8 * * ?")` che calcola la media dei voti conseguiti da ogni alunno alle 8 di ogni giorno e la salva nel campo `mediaVoti` dell'entità `Alunno`.

### Modifiche alle API

A seguito di queste modifiche, sono state apportate le seguenti modifiche alle API esistenti e sono state aggiunte nuove API relative all'entità `AlunnoCompito` nel file `MySchool.postman_collection.json_v2.0.json`.
