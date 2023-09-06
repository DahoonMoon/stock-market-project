package com.project.stock.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class StockTotalViewDto {

	private Integer stockId;
	private Long totalCount;

}
