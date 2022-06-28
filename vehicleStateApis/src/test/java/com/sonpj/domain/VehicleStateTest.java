package com.sonpj.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sonpj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehicleStateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleState.class);
        VehicleState vehicleState1 = new VehicleState();
        vehicleState1.setId(1L);
        VehicleState vehicleState2 = new VehicleState();
        vehicleState2.setId(vehicleState1.getId());
        assertThat(vehicleState1).isEqualTo(vehicleState2);
        vehicleState2.setId(2L);
        assertThat(vehicleState1).isNotEqualTo(vehicleState2);
        vehicleState1.setId(null);
        assertThat(vehicleState1).isNotEqualTo(vehicleState2);
    }
}
