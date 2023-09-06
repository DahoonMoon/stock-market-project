package com.project.stock.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceUtilTest {

    @Test
    @DisplayName("[Util] 호가 계산 테스트")
    void testGetAskingPrice() {
        assertEquals(BigDecimal.valueOf(1), PriceUtil.getAskingPrice(BigDecimal.valueOf(1400)));
        assertEquals(BigDecimal.valueOf(5), PriceUtil.getAskingPrice(BigDecimal.valueOf(2500)));
        assertEquals(BigDecimal.valueOf(10), PriceUtil.getAskingPrice(BigDecimal.valueOf(12000)));
        assertEquals(BigDecimal.valueOf(50), PriceUtil.getAskingPrice(BigDecimal.valueOf(25000)));
        assertEquals(BigDecimal.valueOf(100), PriceUtil.getAskingPrice(BigDecimal.valueOf(57000)));
        assertEquals(BigDecimal.valueOf(500), PriceUtil.getAskingPrice(BigDecimal.valueOf(210000)));
        assertEquals(BigDecimal.valueOf(1000), PriceUtil.getAskingPrice(BigDecimal.valueOf(500000)));
    }
}