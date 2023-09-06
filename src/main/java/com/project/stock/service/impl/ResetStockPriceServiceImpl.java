package com.project.stock.service.impl;

import com.project.stock.common.util.DateTimeUtil;
import com.project.stock.common.util.PriceUtil;
import com.project.stock.model.dto.CsvDto;
import com.project.stock.model.entity.stock.Stock;
import com.project.stock.model.entity.stock.StockPrice;
import com.project.stock.repository.dto.StockPriceDto;
import com.project.stock.repository.stock.CustomStockPriceRepository;
import com.project.stock.repository.stock.StockPriceRepository;
import com.project.stock.repository.stock.StockRepository;
import com.project.stock.service.ResetStockPriceService;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ResetStockPriceServiceImpl implements ResetStockPriceService {

	private final StockRepository stockRepository;
	private final StockPriceRepository stockPriceRepository;
	private final CustomStockPriceRepository customStockPriceRepository;
	Random random = new Random();

	@Override
	public void resetStockPrice() {

		stockPriceRepository.deleteAllInBatch();

		createNewStockPrice(LocalDateTime.now());
	}

	@Override
	public void createNewStockPrice(LocalDateTime localDateTime) {
		log.info("insertNewStockPrice Start");

		LocalDateTime lastTradeEndDateTime = DateTimeUtil.getLastTradeEndDateTime(localDateTime);
		LocalDateTime lastLastTradeEndDateTime = DateTimeUtil.getLastTradeEndDateTime(lastTradeEndDateTime.minusDays(1));

		setInitStockPrice(lastLastTradeEndDateTime.withHour(16).withMinute(0).withSecond(0).withNano(0));
		setAllDayStockPrice(lastTradeEndDateTime);

		log.info("insertNewStockPrice End");
	}

	public void setInitStockPrice(LocalDateTime localDateTime) {
		try {
			String csvFilePath = "init_data/SampleData.csv";

			Reader reader = new InputStreamReader(new ClassPathResource(csvFilePath).getInputStream());
			HeaderColumnNameMappingStrategy<CsvDto> strategy = new HeaderColumnNameMappingStrategy<>();
			strategy.setType(CsvDto.class);

			List<CsvDto> csvDtoList = new CsvToBeanBuilder<CsvDto>(reader)
					.withMappingStrategy(strategy)
					.build()
					.parse();

			List<StockPrice> stockPriceList = new ArrayList<>();

			List<Stock> stockList = stockRepository.findAll();
			Map<Integer, Stock> stockMap = stockList.stream()
					.collect(Collectors.toMap(Stock::getId, Function.identity()));

			csvDtoList.forEach(csvDto -> {
				Stock stock = stockMap.get(csvDto.getId());

				StockPrice stockPrice = StockPrice.builder()
						.stock(stock)
						.timestamp(localDateTime)
						.timeInterval(5)
						.openPrice(csvDto.getPrice())
						.closePrice(csvDto.getPrice())
						.highPrice(csvDto.getPrice())
						.lowPrice(csvDto.getPrice())
						.volume(BigDecimal.valueOf(0))
						.remark("initial Data")
						.build();

				stockPriceList.add(stockPrice);
			});

			stockPriceRepository.saveAll(stockPriceList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void setAllDayStockPrice(LocalDateTime lastTradeEndDateTime) {
		LocalDateTime tradeStartDateTime = lastTradeEndDateTime.withHour(9).withMinute(0).withSecond(0).withNano(0);
		LocalDateTime tradeEndDateTime = lastTradeEndDateTime;

		//            가장 최근 가격 데이터 가져오기
		List<Stock> stockList = stockRepository.findAll();

		List<StockPriceDto> latestStockPriceList = customStockPriceRepository.findAllLatestStockPrice();
		Map<Integer, BigDecimal> latestStockPriceMap = new HashMap<>();

		latestStockPriceList.forEach(
				stockPrice -> latestStockPriceMap.put(stockPrice.getStockId(), stockPrice.getClosePrice()));

		int interval = 5;

		Stream.iterate(tradeStartDateTime, time -> time.plusMinutes(interval))
				.limit(Duration.between(tradeStartDateTime, tradeEndDateTime).toMinutes() / interval + 1)
				.forEach(time -> {
					List<StockPrice> stockPriceList = generateRandomStockPrice(latestStockPriceMap, stockList, time);

					stockPriceList.forEach(stockPrice ->
							latestStockPriceMap.put(stockPrice.getStock().getId(), stockPrice.getClosePrice()));

					stockPriceRepository.saveAll(stockPriceList);
				});
	}

	public List<StockPrice> generateRandomStockPrice(Map<Integer, BigDecimal> latestStockPriceMap,
			List<Stock> stockList,
			LocalDateTime localDateTime) {

		List<StockPrice> stockPriceList = new ArrayList<>();
		stockList.forEach(stock -> {
//                이전 가격
			BigDecimal previousPrice = latestStockPriceMap.get(stock.getId());

//                임의 가격 변동(+- 10호가 임의 변동)
			BigDecimal priceChange = PriceUtil.getAskingPrice(previousPrice)
					.multiply(BigDecimal.valueOf(random.nextInt(21) - 10));
			BigDecimal newPrice = previousPrice.add(priceChange);

//			가격은 0보다는 커야함
			if(newPrice.compareTo(BigDecimal.ZERO) < 0){
				newPrice = BigDecimal.ONE;
			}

//                랜덤 거래량 생성
			BigDecimal volume = BigDecimal.valueOf(random.nextInt(10000) + 1);

			StockPrice stockPrice = StockPrice.builder()
					.stock(stock)
					.timestamp(localDateTime)
					.timeInterval(5)
					.openPrice(previousPrice)
					.closePrice(newPrice)
					.highPrice(previousPrice.compareTo(newPrice) >= 0 ? previousPrice : newPrice)
					.lowPrice(previousPrice.compareTo(newPrice) < 0 ? previousPrice : newPrice)
					.volume(volume)
					.build();

			stockPriceList.add(stockPrice);
		});

		return stockPriceList;
	}


}
