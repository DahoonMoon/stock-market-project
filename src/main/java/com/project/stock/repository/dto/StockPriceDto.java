package com.project.stock.repository.dto;

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
public class StockPriceDto {

	private Integer stockId;
	private String stockCode;
	private String stockName;
	private BigDecimal closePrice;

}
