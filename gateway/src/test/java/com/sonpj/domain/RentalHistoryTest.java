package com.sonpj.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sonpj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RentalHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RentalHistory.class);
        RentalHistory rentalHistory1 = new RentalHistory();
        rentalHistory1.setId(1L);
        RentalHistory rentalHistory2 = new RentalHistory();
        rentalHistory2.setId(rentalHistory1.getId());
        assertThat(rentalHistory1).isEqualTo(rentalHistory2);
        rentalHistory2.setId(2L);
        assertThat(rentalHistory1).isNotEqualTo(rentalHistory2);
        rentalHistory1.setId(null);
        assertThat(rentalHistory1).isNotEqualTo(rentalHistory2);
    }
}
