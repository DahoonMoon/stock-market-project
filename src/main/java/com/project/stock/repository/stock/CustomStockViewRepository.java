package com.project.stock.repository.stock;

import com.project.stock.repository.dto.StockTotalViewDto;
import java.time.LocalDateTime;
import java.util.List;

public interface CustomStockViewRepository {


    List<StockTotalViewDto> findStockTotalViewAfterDate(LocalDateTime date);

}
