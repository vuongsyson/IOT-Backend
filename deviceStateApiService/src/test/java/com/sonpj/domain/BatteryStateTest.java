package com.sonpj.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sonpj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BatteryStateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BatteryState.class);
        BatteryState batteryState1 = new BatteryState();
        batteryState1.setId(1L);
        BatteryState batteryState2 = new BatteryState();
        batteryState2.setId(batteryState1.getId());
        assertThat(batteryState1).isEqualTo(batteryState2);
        batteryState2.setId(2L);
        assertThat(batteryState1).isNotEqualTo(batteryState2);
        batteryState1.setId(null);
        assertThat(batteryState1).isNotEqualTo(batteryState2);
    }
}
