package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlunnoCompitoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlunnoCompitoDTO.class);
        AlunnoCompitoDTO alunnoCompitoDTO1 = new AlunnoCompitoDTO();
        alunnoCompitoDTO1.setId(1L);
        AlunnoCompitoDTO alunnoCompitoDTO2 = new AlunnoCompitoDTO();
        assertThat(alunnoCompitoDTO1).isNotEqualTo(alunnoCompitoDTO2);
        alunnoCompitoDTO2.setId(alunnoCompitoDTO1.getId());
        assertThat(alunnoCompitoDTO1).isEqualTo(alunnoCompitoDTO2);
        alunnoCompitoDTO2.setId(2L);
        assertThat(alunnoCompitoDTO1).isNotEqualTo(alunnoCompitoDTO2);
        alunnoCompitoDTO1.setId(null);
        assertThat(alunnoCompitoDTO1).isNotEqualTo(alunnoCompitoDTO2);
    }
}
