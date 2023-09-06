package com.project.stock.model.dto.response.stock;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockResponseDto {

    private Integer order;
    private Integer stockId;
    private String stockName;
    private String stockCode;
    private BigDecimal price;
    private BigDecimal priceChangeRate;
    private Long totalView;
    private BigDecimal totalVolume;
}
