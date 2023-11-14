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
