package com.project.stock.repository.stock.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.stock.config.TestConfig;
import com.project.stock.model.entity.stock.Stock;
import com.project.stock.model.entity.stock.StockPrice;
import com.project.stock.repository.dto.StockPriceDto;
import com.project.stock.repository.dto.StockTotalVolumeDto;
import com.project.stock.repository.stock.CustomStockPriceRepository;
import com.project.stock.repository.stock.StockPriceRepository;
import com.project.stock.repository.stock.StockRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import({TestConfig.class, CustomStockPriceRepositoryImpl.class})
public class CustomStockPriceRepositoryImplTest {

	@Autowired
	private CustomStockPriceRepository customStockPriceRepository;
	@Autowired
	private StockRepository stockRepository;
	@Autowired
	private StockPriceRepository stockPriceRepository;

	LocalDateTime mockDateTime = LocalDateTime.of(2023, 6, 20, 12, 0, 0);

	@BeforeEach
	public void setStockPriceData() {
		Stock mockStock = Stock.builder()
				.stockCode("000001")
				.stockName("카카오페이증권")
				.build();

		StockPrice mockStockPrice = StockPrice.builder()
				.stock(mockStock)
				.timestamp(mockDateTime)
				.timeInterval(1)
				.openPrice(BigDecimal.valueOf(100))
				.closePrice(BigDecimal.valueOf(100))
				.highPrice(BigDecimal.valueOf(100))
				.lowPrice(BigDecimal.valueOf(100))
				.volume(BigDecimal.valueOf(100))
				.build();

		stockRepository.save(mockStock);
		stockPriceRepository.save(mockStockPrice);
	}

	@Test
	@DisplayName("[QueryDSL] stock_id 별 종가 테스트")
	void findAllLatestStockPriceTest() {

		List<StockPriceDto> result = customStockPriceRepository.findAllLatestStockPrice();

		assertThat(result.size() > 0).isTrue();
	}

	@Test
	@DisplayName("[QueryDSL] stock_id 별 거래량 총합 테스트")
	void findStockTotalVolumeTest() {

		List<StockTotalVolumeDto> result = customStockPriceRepository.findStockTotalVolume(mockDateTime);

		assertThat(result.size() > 0).isTrue();
	}

	@Test
	@DisplayName("[QueryDSL] stock_id 별 특정 시간 사이의 종가 테스트")
	void findStockLastPriceTest() {

		List<StockPriceDto> result = customStockPriceRepository.findStockLastPrice(mockDateTime);

		assertThat(result.size() > 0).isTrue();
	}
}