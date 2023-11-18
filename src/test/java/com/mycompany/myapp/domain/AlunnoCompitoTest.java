package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AlunnoCompitoTestSamples.*;
import static com.mycompany.myapp.domain.AlunnoTestSamples.*;
import static com.mycompany.myapp.domain.CompitoInClasseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlunnoCompitoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlunnoCompito.class);
        AlunnoCompito alunnoCompito1 = getAlunnoCompitoSample1();
        AlunnoCompito alunnoCompito2 = new AlunnoCompito();
        assertThat(alunnoCompito1).isNotEqualTo(alunnoCompito2);

        alunnoCompito2.setId(alunnoCompito1.getId());
        assertThat(alunnoCompito1).isEqualTo(alunnoCompito2);

        alunnoCompito2 = getAlunnoCompitoSample2();
        assertThat(alunnoCompito1).isNotEqualTo(alunnoCompito2);
    }

    @Test
    void alunnoTest() throws Exception {
        AlunnoCompito alunnoCompito = getAlunnoCompitoRandomSampleGenerator();
        Alunno alunnoBack = getAlunnoRandomSampleGenerator();

        alunnoCompito.setAlunno(alunnoBack);
        assertThat(alunnoCompito.getAlunno()).isEqualTo(alunnoBack);

        alunnoCompito.alunno(null);
        assertThat(alunnoCompito.getAlunno()).isNull();
    }

    @Test
    void compitoTest() throws Exception {
        AlunnoCompito alunnoCompito = getAlunnoCompitoRandomSampleGenerator();
        CompitoInClasse compitoInClasseBack = getCompitoInClasseRandomSampleGenerator();

        alunnoCompito.setCompito(compitoInClasseBack);
        assertThat(alunnoCompito.getCompito()).isEqualTo(compitoInClasseBack);

        alunnoCompito.compito(null);
        assertThat(alunnoCompito.getCompito()).isNull();
    }
}
