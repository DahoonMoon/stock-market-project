package com.project.stock.service;

import com.project.stock.common.code.SortField;
import com.project.stock.common.code.SortOrder;
import com.project.stock.model.dto.response.stock.StockResponseDto;
import java.util.List;

public interface SortStockService {

	List<StockResponseDto> getSortedStockList(SortField sortField, SortOrder sortOrder, int page, int size);

}
