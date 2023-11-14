package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AlunnoTestSamples.*;
import static com.mycompany.myapp.domain.ClasseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ClasseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Classe.class);
        Classe classe1 = getClasseSample1();
        Classe classe2 = new Classe();
        assertThat(classe1).isNotEqualTo(classe2);

        classe2.setId(classe1.getId());
        assertThat(classe1).isEqualTo(classe2);

        classe2 = getClasseSample2();
        assertThat(classe1).isNotEqualTo(classe2);
    }

    @Test
    void alunniTest() throws Exception {
        Classe classe = getClasseRandomSampleGenerator();
        Alunno alunnoBack = getAlunnoRandomSampleGenerator();

        classe.addAlunni(alunnoBack);
        assertThat(classe.getAlunnis()).containsOnly(alunnoBack);
        assertThat(alunnoBack.getClasseDiAppartenenza()).isEqualTo(classe);

        classe.removeAlunni(alunnoBack);
        assertThat(classe.getAlunnis()).doesNotContain(alunnoBack);
        assertThat(alunnoBack.getClasseDiAppartenenza()).isNull();

        classe.alunnis(new HashSet<>(Set.of(alunnoBack)));
        assertThat(classe.getAlunnis()).containsOnly(alunnoBack);
        assertThat(alunnoBack.getClasseDiAppartenenza()).isEqualTo(classe);

        classe.setAlunnis(new HashSet<>());
        assertThat(classe.getAlunnis()).doesNotContain(alunnoBack);
        assertThat(alunnoBack.getClasseDiAppartenenza()).isNull();
    }
}
