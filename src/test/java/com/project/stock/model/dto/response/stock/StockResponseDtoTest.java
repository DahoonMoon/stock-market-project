package com.project.stock.model.dto.response.stock;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StockResponseDtoTest {

    @Test
    @DisplayName("[DTO] builder 테스트")
    void builder() {
//        given
//        when
        StockResponseDto mockStockResponseDto = StockResponseDto.builder()
            .order(1)
            .stockId(1)
            .stockCode("000001")
            .stockName("카카오페이증권")
            .price(BigDecimal.valueOf(10000))
            .priceChangeRate(BigDecimal.valueOf(0.3000))
            .totalVolume(BigDecimal.valueOf(100))
            .totalView(1000L)
            .build();

//        then
        assertThat(mockStockResponseDto).isNotNull();
    }
}