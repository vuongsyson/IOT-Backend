package com.sonpj.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sonpj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SosTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SosType.class);
        SosType sosType1 = new SosType();
        sosType1.setId(1L);
        SosType sosType2 = new SosType();
        sosType2.setId(sosType1.getId());
        assertThat(sosType1).isEqualTo(sosType2);
        sosType2.setId(2L);
        assertThat(sosType1).isNotEqualTo(sosType2);
        sosType1.setId(null);
        assertThat(sosType1).isNotEqualTo(sosType2);
    }
}
