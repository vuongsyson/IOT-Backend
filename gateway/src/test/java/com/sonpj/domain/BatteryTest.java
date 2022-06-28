package com.sonpj.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sonpj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BatteryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Battery.class);
        Battery battery1 = new Battery();
        battery1.setId(1L);
        Battery battery2 = new Battery();
        battery2.setId(battery1.getId());
        assertThat(battery1).isEqualTo(battery2);
        battery2.setId(2L);
        assertThat(battery1).isNotEqualTo(battery2);
        battery1.setId(null);
        assertThat(battery1).isNotEqualTo(battery2);
    }
}
