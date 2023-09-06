package com.project.stock.controller;

import com.project.stock.common.code.SortField;
import com.project.stock.common.code.SortOrder;
import com.project.stock.model.dto.response.CommonResponse;
import com.project.stock.model.dto.response.stock.StockResponseDto;
import com.project.stock.service.SortStockService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Sort Stock", description = "주식 데이터를 가격변동률, 거래량, 인기도 순으로 정렬하는 API 입니다.")
@RequiredArgsConstructor
@RequestMapping("/stocks")
@RestController
public class SortStockController {

	private final SortStockService sortStockService;

	@GetMapping
	public CommonResponse<List<StockResponseDto>> getSortedStocks(
			@RequestParam(value = "sortField") SortField sortField,
			@RequestParam(value = "sortOrder", defaultValue = "DESCENDING") SortOrder sortOrder,
			@RequestParam(value = "page", defaultValue = "1") @Min(value = 1) int page,
			@RequestParam(value = "size", defaultValue = "100") @Min(value = 1) int size) {

		return new CommonResponse<>(HttpStatus.OK, sortStockService.getSortedStockList(sortField, sortOrder, page, size));
	}


}
