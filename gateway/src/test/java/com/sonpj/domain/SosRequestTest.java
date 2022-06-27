package com.sonpj.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sonpj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SosRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SosRequest.class);
        SosRequest sosRequest1 = new SosRequest();
        sosRequest1.setId(1L);
        SosRequest sosRequest2 = new SosRequest();
        sosRequest2.setId(sosRequest1.getId());
        assertThat(sosRequest1).isEqualTo(sosRequest2);
        sosRequest2.setId(2L);
        assertThat(sosRequest1).isNotEqualTo(sosRequest2);
        sosRequest1.setId(null);
        assertThat(sosRequest1).isNotEqualTo(sosRequest2);
    }
}
