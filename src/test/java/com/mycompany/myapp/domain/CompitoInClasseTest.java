package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AlunnoTestSamples.*;
import static com.mycompany.myapp.domain.CompitoInClasseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompitoInClasseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompitoInClasse.class);
        CompitoInClasse compitoInClasse1 = getCompitoInClasseSample1();
        CompitoInClasse compitoInClasse2 = new CompitoInClasse();
        assertThat(compitoInClasse1).isNotEqualTo(compitoInClasse2);

        compitoInClasse2.setId(compitoInClasse1.getId());
        assertThat(compitoInClasse1).isEqualTo(compitoInClasse2);

        compitoInClasse2 = getCompitoInClasseSample2();
        assertThat(compitoInClasse1).isNotEqualTo(compitoInClasse2);
    }

    @Test
    void alunnoDiRiferimentoTest() throws Exception {
        CompitoInClasse compitoInClasse = getCompitoInClasseRandomSampleGenerator();
        Alunno alunnoBack = getAlunnoRandomSampleGenerator();

        compitoInClasse.setAlunnoDiRiferimento(alunnoBack);
        assertThat(compitoInClasse.getAlunnoDiRiferimento()).isEqualTo(alunnoBack);

        compitoInClasse.alunnoDiRiferimento(null);
        assertThat(compitoInClasse.getAlunnoDiRiferimento()).isNull();
    }
}
