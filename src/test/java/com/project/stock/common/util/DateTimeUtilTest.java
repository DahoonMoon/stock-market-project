package com.project.stock.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DateTimeUtilTest {

    @Test
    @DisplayName("[Util] 거래가능일 판별 테스트")
    void testIsTradeDate() {
        // 평일
//        given
//        when
        LocalDateTime weekday = LocalDateTime.of(2023, 6, 19, 10, 0);
//        then
        assertTrue(DateTimeUtil.isTradeDate(weekday));

        // 주말
//        given
//        when
        LocalDateTime weekend = LocalDateTime.of(2023, 6, 18, 10, 0);
//        then
        assertFalse(DateTimeUtil.isTradeDate(weekend));
    }

    @Test
    @DisplayName("[Util] 최종 거래 시각 추출 테스트")
    void testGetLastTradeEndDateTime() {

//        평일, 거래시간 이전 ->  당시
//        given
//        when
        LocalDateTime beforeTradeStart = LocalDateTime.of(2023, 6, 19, 8, 0);
//        then
        assertEquals(LocalDateTime.of(2023, 6, 16, 16, 0), DateTimeUtil.getLastTradeEndDateTime(beforeTradeStart));

//        평일, 거래가능시간 ->  당시
//        given
//        when
        LocalDateTime beforeTradeEnd = LocalDateTime.of(2023, 6, 19, 15, 0);
//        then
        assertEquals(LocalDateTime.of(2023, 6, 19, 15, 0), DateTimeUtil.getLastTradeEndDateTime(beforeTradeEnd));

//        평일, 거래시간 이후 -> 당일 16시
//        given
//        when
        LocalDateTime afterTradeEnd = LocalDateTime.of(2023, 6, 19, 17, 0);
//        then
        assertEquals(LocalDateTime.of(2023, 6, 19, 16, 0), DateTimeUtil.getLastTradeEndDateTime(afterTradeEnd));

//        주말 -> 마지막 거래가능일 16시
//        given
//        when
        LocalDateTime weekend = LocalDateTime.of(2023, 6, 18, 10, 0);
//        then
        assertEquals(LocalDateTime.of(2023, 6, 16, 16, 0), DateTimeUtil.getLastTradeEndDateTime(weekend));
    }
}