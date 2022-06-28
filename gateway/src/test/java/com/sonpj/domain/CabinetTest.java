package com.sonpj.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sonpj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CabinetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cabinet.class);
        Cabinet cabinet1 = new Cabinet();
        cabinet1.setId(1L);
        Cabinet cabinet2 = new Cabinet();
        cabinet2.setId(cabinet1.getId());
        assertThat(cabinet1).isEqualTo(cabinet2);
        cabinet2.setId(2L);
        assertThat(cabinet1).isNotEqualTo(cabinet2);
        cabinet1.setId(null);
        assertThat(cabinet1).isNotEqualTo(cabinet2);
    }
}
