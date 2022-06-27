package com.sonpj.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sonpj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RefreshTokenTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RefreshToken.class);
        RefreshToken refreshToken1 = new RefreshToken();
        refreshToken1.setId(1L);
        RefreshToken refreshToken2 = new RefreshToken();
        refreshToken2.setId(refreshToken1.getId());
        assertThat(refreshToken1).isEqualTo(refreshToken2);
        refreshToken2.setId(2L);
        assertThat(refreshToken1).isNotEqualTo(refreshToken2);
        refreshToken1.setId(null);
        assertThat(refreshToken1).isNotEqualTo(refreshToken2);
    }
}
