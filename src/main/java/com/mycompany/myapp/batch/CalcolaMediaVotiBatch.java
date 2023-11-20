package com.mycompany.myapp.batch;

import com.mycompany.myapp.domain.Alunno;
import com.mycompany.myapp.domain.AlunnoCompito;
import com.mycompany.myapp.repository.AlunnoRepository;
import java.util.List;
import java.util.Set;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CalcolaMediaVotiBatch {

    private final AlunnoRepository alunnoRepository;

    public CalcolaMediaVotiBatch(AlunnoRepository alunnoRepository) {
        this.alunnoRepository = alunnoRepository;
    }

    @Scheduled(cron = "0 08 18 * * ?")
    public void calcolaMediaVotiAlunni() {
        // Recupera tutti gli alunni con i compiti
        List<Alunno> alunniWithCompiti = alunnoRepository.findAllWithCompiti();

        // Calcola la media dei voti per ogni alunno
        for (Alunno alunno : alunniWithCompiti) {
            Set<AlunnoCompito> compiti = alunno.getCompitiEseguitis();
            if (compiti != null && !compiti.isEmpty()) {
                double mediaVoti = compiti.stream().mapToDouble(AlunnoCompito::getRisultatoNumerico).average().orElse(0.0);

                // Aggiorna il campo mediaVoti nell'entità Alunno
                alunno.setMediaVoti(mediaVoti);
                alunnoRepository.save(alunno);
            }
        }
    }
}
