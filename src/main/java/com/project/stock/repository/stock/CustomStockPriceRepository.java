package com.project.stock.repository.stock;

import com.project.stock.repository.dto.StockPriceDto;
import com.project.stock.repository.dto.StockTotalVolumeDto;
import java.time.LocalDateTime;
import java.util.List;

public interface CustomStockPriceRepository {

    List<StockPriceDto> findAllLatestStockPrice();
    List<StockTotalVolumeDto> findStockTotalVolume(LocalDateTime localDateTime);
    List<StockPriceDto> findStockLastPrice(LocalDateTime localDateTime);

}
