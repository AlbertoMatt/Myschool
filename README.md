CREDENZIALI DI ACCESSO DI UN UTENTE DI DEFAULT GENERATO DA JHIPSTER
Username: "admin"
Password: "admin"

API
Elenco delle Classi (API Pubblica)
Endpoint: /api/classes
Metodo: GET
Descrizione: Restituisce l'elenco delle classi della scuola.

Altre API (Richiedono Autenticazione)
Le altre API, come quelle per gestire gli alunni e i compiti in classe, richiedono l'autenticazione tramite l'inserimento di un token di autenticazione.

CONFIGURAZIONE DEL DATABASE
Modifica al file src/main/resources/config/application-dev.yml per configurare l'accesso al database, inserendo nel datasource il nome del database e la password di connessione al database.

AVVIO DELL'APPLICAZIONE
Digitando il comando ./mvnw da terminale nella cartella dove è presente il progetto.
