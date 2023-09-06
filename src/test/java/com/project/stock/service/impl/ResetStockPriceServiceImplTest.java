package com.project.stock.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.stock.common.util.PriceUtil;
import com.project.stock.model.dto.CsvDto;
import com.project.stock.model.entity.stock.Stock;
import com.project.stock.model.entity.stock.StockPrice;
import com.project.stock.repository.dto.StockPriceDto;
import com.project.stock.repository.stock.CustomStockPriceRepository;
import com.project.stock.repository.stock.StockPriceRepository;
import com.project.stock.repository.stock.StockRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ResetStockPriceServiceImplTest {

	@Mock
	private StockRepository stockRepository;
	@Mock
	private StockPriceRepository stockPriceRepository;
	@Mock
	private CustomStockPriceRepository customStockPriceRepository;

	@Spy
	@InjectMocks
	private ResetStockPriceServiceImpl resetStockPriceService;
	@Mock
	Random random;
	private static MockedStatic<PriceUtil> mockPriceUtil;

	@BeforeAll
	public static void beforeAll() {
		mockPriceUtil = mockStatic(PriceUtil.class);
	}

	@Test
	@DisplayName("[Service] 초기 랜덤 가격 데이터 Insert 테스트")
	void setInitStockPriceTest() {
//        given
		CsvDto mockCsvDto = new CsvDto();
		mockCsvDto.setId(1);
		mockCsvDto.setPrice(BigDecimal.valueOf(100));
		List<CsvDto> mockCsvDtoList = List.of(mockCsvDto);

		Stock mockStock = Stock.builder().id(1).stockCode("000001").stockName("카카오페이증권").build();
		List<Stock> mockStockList = List.of(mockStock);

		LocalDateTime mockDateTime = LocalDateTime.now();

		when(stockRepository.findAll()).thenReturn(mockStockList);

//        when
		resetStockPriceService.setInitStockPrice(mockDateTime);

//        then
		List<StockPrice> expectedStockPriceList = List.of(StockPrice.builder()
				.stock(mockStock)
				.timestamp(mockDateTime)
				.timeInterval(5)
				.openPrice(BigDecimal.valueOf(100))
				.closePrice(BigDecimal.valueOf(100))
				.highPrice(BigDecimal.valueOf(100))
				.lowPrice(BigDecimal.valueOf(100))
				.volume(BigDecimal.valueOf(0))
				.remark("initial Data")
				.build());

		ArgumentCaptor<List<StockPrice>> argument = ArgumentCaptor.forClass(List.class);
//        saveAll 호출 및 인자 검증
		verify(stockPriceRepository, atLeastOnce()).saveAll(argument.capture());

//        데이터 검증
		List<StockPrice> capturedStockPrices = argument.getValue();
		assertEquals(1, capturedStockPrices.get(0).getStock().getId());
	}

	@Test
	@DisplayName("[Service] 일간 랜덤 가격 데이터 Insert 테스트")
	void setAllDayStockPriceTest() {
		// given
		LocalDateTime mockDateTime = LocalDateTime.now();

		Stock mockStock = Stock.builder()
				.id(1)
				.stockCode("000001")
				.stockName("카카오페이증권")
				.build();
		List<Stock> mockStockList = List.of(mockStock);

		StockPriceDto mockStockPriceDto = StockPriceDto.builder()
				.stockId(1)
				.closePrice(BigDecimal.valueOf(1000))
				.build();
		List<StockPriceDto> mockStockPriceList = List.of(mockStockPriceDto);

		StockPrice mockStockPrice = StockPrice.builder()
				.stock(mockStock)
				.openPrice(BigDecimal.valueOf(1000))
				.closePrice(BigDecimal.valueOf(1000))
				.highPrice(BigDecimal.valueOf(1000))
				.lowPrice(BigDecimal.valueOf(1000))
				.timeInterval(10)
				.timestamp(mockDateTime)
				.volume(BigDecimal.valueOf(1000))
				.build();
		List<StockPrice> mockStockPrices = List.of(mockStockPrice);

		when(stockRepository.findAll()).thenReturn(mockStockList);
		when(customStockPriceRepository.findAllLatestStockPrice()).thenReturn(mockStockPriceList);
		when(stockPriceRepository.saveAll(any())).thenAnswer(i -> i.getArguments()[0]);
		doReturn(mockStockPrices).when(resetStockPriceService).generateRandomStockPrice(any(), any(), any());

//        when
		resetStockPriceService.setAllDayStockPrice(mockDateTime);

//        then
		ArgumentCaptor<List<StockPrice>> argument = ArgumentCaptor.forClass(List.class);
//        saveAll 호출 및 인자 검증
		verify(stockPriceRepository, atLeastOnce()).saveAll(argument.capture());

//        데이터 검증
		List<StockPrice> capturedStockPrices = argument.getValue();
		assertEquals(1, capturedStockPrices.get(0).getStock().getId());
	}


	@Test
	@DisplayName("[Service] 랜덤한 가격, 거래량 생성 테스트")
	void generateRandomStockPriceTest() {
//        given
		Map<Integer, BigDecimal> mockStockPriceMap = new HashMap<>();
		mockStockPriceMap.put(1, BigDecimal.valueOf(100));

		Stock mockStock = Stock.builder()
				.id(1)
				.stockCode("000001")
				.stockName("카카오페이증권")
				.build();
		List<Stock> mockStockList = List.of(mockStock);

		LocalDateTime mockDateTime = LocalDateTime.now();

		when(PriceUtil.getAskingPrice(BigDecimal.valueOf(100))).thenReturn(BigDecimal.valueOf(10));
		lenient().when(random.nextInt(21)).thenReturn(10);
		lenient().when(random.nextInt(10000)).thenReturn(5000);

//        when
		List<StockPrice> result = resetStockPriceService.generateRandomStockPrice(mockStockPriceMap, mockStockList, mockDateTime);

//        then
		assertEquals(1, result.size());

//        데이터 검증
		StockPrice stockPrice = result.get(0);

		assertEquals(mockStock, stockPrice.getStock());
		assertEquals(mockDateTime, stockPrice.getTimestamp());
		assertEquals(BigDecimal.valueOf(100), stockPrice.getOpenPrice());

		assertTrue(stockPrice.getClosePrice().compareTo(BigDecimal.valueOf(100).add(BigDecimal.valueOf(100))) <= 0);
		assertTrue(stockPrice.getClosePrice().compareTo(BigDecimal.valueOf(100).subtract(BigDecimal.valueOf(100))) >= 0);

		assertTrue(stockPrice.getVolume().compareTo(BigDecimal.valueOf(1)) >= 0);
		assertTrue(stockPrice.getVolume().compareTo(BigDecimal.valueOf(10000)) <= 0);
	}
}