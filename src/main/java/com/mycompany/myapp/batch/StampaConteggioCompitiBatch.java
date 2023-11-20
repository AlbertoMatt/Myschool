package com.mycompany.myapp.batch;

import com.mycompany.myapp.repository.CompitoInClasseRepository;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StampaConteggioCompitiBatch {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final CompitoInClasseRepository compitoInClasseRepository;

    public StampaConteggioCompitiBatch(CompitoInClasseRepository compitoInClasseRepository) {
        this.compitoInClasseRepository = compitoInClasseRepository;
    }

    @Scheduled(cron = "0 0 12 * * ?")
    public void stampaConteggioCompiti() {
        LocalDate dataSetteGiorniPrima = LocalDate.now().minusDays(7);
        int conteggioCompiti = compitoInClasseRepository.countCompitiByData(dataSetteGiorniPrima);
        log.debug("Numero di compiti eseguiti 7 giorni prima: " + conteggioCompiti);
    }
}
