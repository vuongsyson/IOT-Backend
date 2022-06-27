package com.sonpj.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sonpj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BpSwapRecordTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BpSwapRecord.class);
        BpSwapRecord bpSwapRecord1 = new BpSwapRecord();
        bpSwapRecord1.setId(1L);
        BpSwapRecord bpSwapRecord2 = new BpSwapRecord();
        bpSwapRecord2.setId(bpSwapRecord1.getId());
        assertThat(bpSwapRecord1).isEqualTo(bpSwapRecord2);
        bpSwapRecord2.setId(2L);
        assertThat(bpSwapRecord1).isNotEqualTo(bpSwapRecord2);
        bpSwapRecord1.setId(null);
        assertThat(bpSwapRecord1).isNotEqualTo(bpSwapRecord2);
    }
}
