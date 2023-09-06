package com.project.stock.repository.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StockTotalVolumeDto {

	private Integer stockId;
	private BigDecimal totalVolume;

}
