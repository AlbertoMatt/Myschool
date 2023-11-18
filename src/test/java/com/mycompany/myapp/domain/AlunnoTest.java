package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AlunnoCompitoTestSamples.*;
import static com.mycompany.myapp.domain.AlunnoTestSamples.*;
import static com.mycompany.myapp.domain.ClasseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AlunnoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Alunno.class);
        Alunno alunno1 = getAlunnoSample1();
        Alunno alunno2 = new Alunno();
        assertThat(alunno1).isNotEqualTo(alunno2);

        alunno2.setId(alunno1.getId());
        assertThat(alunno1).isEqualTo(alunno2);

        alunno2 = getAlunnoSample2();
        assertThat(alunno1).isNotEqualTo(alunno2);
    }

    @Test
    void compitiEseguitiTest() throws Exception {
        Alunno alunno = getAlunnoRandomSampleGenerator();
        AlunnoCompito alunnoCompitoBack = getAlunnoCompitoRandomSampleGenerator();

        alunno.addCompitiEseguiti(alunnoCompitoBack);
        assertThat(alunno.getCompitiEseguitis()).containsOnly(alunnoCompitoBack);
        assertThat(alunnoCompitoBack.getAlunno()).isEqualTo(alunno);

        alunno.removeCompitiEseguiti(alunnoCompitoBack);
        assertThat(alunno.getCompitiEseguitis()).doesNotContain(alunnoCompitoBack);
        assertThat(alunnoCompitoBack.getAlunno()).isNull();

        alunno.compitiEseguitis(new HashSet<>(Set.of(alunnoCompitoBack)));
        assertThat(alunno.getCompitiEseguitis()).containsOnly(alunnoCompitoBack);
        assertThat(alunnoCompitoBack.getAlunno()).isEqualTo(alunno);

        alunno.setCompitiEseguitis(new HashSet<>());
        assertThat(alunno.getCompitiEseguitis()).doesNotContain(alunnoCompitoBack);
        assertThat(alunnoCompitoBack.getAlunno()).isNull();
    }

    @Test
    void classeDiAppartenenzaTest() throws Exception {
        Alunno alunno = getAlunnoRandomSampleGenerator();
        Classe classeBack = getClasseRandomSampleGenerator();

        alunno.setClasseDiAppartenenza(classeBack);
        assertThat(alunno.getClasseDiAppartenenza()).isEqualTo(classeBack);

        alunno.classeDiAppartenenza(null);
        assertThat(alunno.getClasseDiAppartenenza()).isNull();
    }
}
