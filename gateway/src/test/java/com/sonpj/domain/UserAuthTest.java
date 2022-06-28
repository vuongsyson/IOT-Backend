package com.sonpj.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sonpj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserAuthTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAuth.class);
        UserAuth userAuth1 = new UserAuth();
        userAuth1.setId(1L);
        UserAuth userAuth2 = new UserAuth();
        userAuth2.setId(userAuth1.getId());
        assertThat(userAuth1).isEqualTo(userAuth2);
        userAuth2.setId(2L);
        assertThat(userAuth1).isNotEqualTo(userAuth2);
        userAuth1.setId(null);
        assertThat(userAuth1).isNotEqualTo(userAuth2);
    }
}
