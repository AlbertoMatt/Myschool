package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AlunnoCompitoTestSamples.*;
import static com.mycompany.myapp.domain.CompitoInClasseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
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
    void alunniTest() throws Exception {
        CompitoInClasse compitoInClasse = getCompitoInClasseRandomSampleGenerator();
        AlunnoCompito alunnoCompitoBack = getAlunnoCompitoRandomSampleGenerator();

        compitoInClasse.addAlunni(alunnoCompitoBack);
        assertThat(compitoInClasse.getAlunnis()).containsOnly(alunnoCompitoBack);
        assertThat(alunnoCompitoBack.getCompito()).isEqualTo(compitoInClasse);

        compitoInClasse.removeAlunni(alunnoCompitoBack);
        assertThat(compitoInClasse.getAlunnis()).doesNotContain(alunnoCompitoBack);
        assertThat(alunnoCompitoBack.getCompito()).isNull();

        compitoInClasse.alunnis(new HashSet<>(Set.of(alunnoCompitoBack)));
        assertThat(compitoInClasse.getAlunnis()).containsOnly(alunnoCompitoBack);
        assertThat(alunnoCompitoBack.getCompito()).isEqualTo(compitoInClasse);

        compitoInClasse.setAlunnis(new HashSet<>());
        assertThat(compitoInClasse.getAlunnis()).doesNotContain(alunnoCompitoBack);
        assertThat(alunnoCompitoBack.getCompito()).isNull();
    }
}
