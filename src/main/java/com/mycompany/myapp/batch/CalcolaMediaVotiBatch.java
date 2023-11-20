package com.mycompany.myapp.batch;

import com.mycompany.myapp.domain.Alunno;
import com.mycompany.myapp.domain.AlunnoCompito;
import com.mycompany.myapp.repository.AlunnoRepository;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CalcolaMediaVotiBatch {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final AlunnoRepository alunnoRepository;

    public CalcolaMediaVotiBatch(AlunnoRepository alunnoRepository) {
        this.alunnoRepository = alunnoRepository;
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void calcolaMediaVotiAlunni() {
        log.debug("calcolaMediaVotiAlunni START");

        List<Alunno> alunniWithCompiti = alunnoRepository.findAllWithCompiti();

        for (Alunno alunno : alunniWithCompiti) {
            Set<AlunnoCompito> compiti = alunno.getCompitiEseguitis();
            if (compiti != null && !compiti.isEmpty()) {
                double mediaVoti = compiti.stream().mapToDouble(AlunnoCompito::getRisultatoNumerico).average().orElse(0.0);
                alunno.setMediaVoti(mediaVoti);
                alunnoRepository.save(alunno);
            }
        }

        log.debug("calcolaMediaVotiAlunni END");
    }
}
