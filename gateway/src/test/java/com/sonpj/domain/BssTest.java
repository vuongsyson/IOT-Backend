package com.sonpj.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sonpj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BssTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bss.class);
        Bss bss1 = new Bss();
        bss1.setId(1L);
        Bss bss2 = new Bss();
        bss2.setId(bss1.getId());
        assertThat(bss1).isEqualTo(bss2);
        bss2.setId(2L);
        assertThat(bss1).isNotEqualTo(bss2);
        bss1.setId(null);
        assertThat(bss1).isNotEqualTo(bss2);
    }
}
