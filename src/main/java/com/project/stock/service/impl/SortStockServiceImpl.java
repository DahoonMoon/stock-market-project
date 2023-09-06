package com.project.stock.service.impl;

import com.project.stock.common.code.ErrorCode;
import com.project.stock.common.code.SortField;
import com.project.stock.common.code.SortOrder;
import com.project.stock.common.exception.PagingException;
import com.project.stock.common.util.DateTimeUtil;
import com.project.stock.model.dto.response.stock.StockResponseDto;
import com.project.stock.repository.dto.StockPriceChangeDto;
import com.project.stock.repository.dto.StockPriceDto;
import com.project.stock.repository.dto.StockTotalViewDto;
import com.project.stock.repository.dto.StockTotalVolumeDto;
import com.project.stock.repository.stock.CustomStockPriceRepository;
import com.project.stock.repository.stock.CustomStockViewRepository;
import com.project.stock.service.SortStockService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SortStockServiceImpl implements SortStockService {

	private final CustomStockViewRepository customStockViewRepository;
	private final CustomStockPriceRepository customStockPriceRepository;


	@Override
	public List<StockResponseDto> getSortedStockList(SortField sortField, SortOrder sortOrder, int page, int size) {
		log.info("{} 순 {} 정렬, {} page, page size : {}", sortField.getDescription(), sortOrder.getDescription(), page, size);

//        집계데이터 세팅
		List<StockResponseDto> stockResponseDtoList = getPriceChangeRateList();
		stockResponseDtoList = setResponseTotalVolume(stockResponseDtoList);
		stockResponseDtoList = setResponseTotalView(stockResponseDtoList);

//        요청한 정렬 필드, 방향에 따라 정렬
		List<StockResponseDto> sortedList = stockResponseDtoList.stream()
				.sorted(getComparator(sortField, sortOrder))
				.collect(Collectors.toList());

//        순번 매기기
		for (int i = 0; i < sortedList.size(); i++) {
			sortedList.get(i).setOrder(i + 1);
		}

//        페이징 처리 하여 리턴
		return getPagedDataList(sortedList, page, size);
	}

	private Comparator<StockResponseDto> getComparator(SortField sortField, SortOrder sortOrder) {
		Comparator<StockResponseDto> comparator = null;

		switch (sortField) {
			case CHANGE:
				comparator = Comparator.comparing(StockResponseDto::getPriceChangeRate);
				break;
			case VOLUME:
				comparator = Comparator.comparing(StockResponseDto::getTotalVolume);
				break;
			case POPULAR:
				comparator = Comparator.comparing(StockResponseDto::getTotalView);
				break;
		}

		if (sortOrder == SortOrder.DESCENDING) {
			comparator = comparator.reversed();
		}

		return comparator;
	}

	private List<StockResponseDto> getPagedDataList(List<StockResponseDto> stockResponseDtoList, int page, int size) {
		int startIndex = (page - 1) * size;
		int endIndex = Math.min(startIndex + size, stockResponseDtoList.size());

//		페이지 범위를 벗어나는 경우 Exception
		if (startIndex >= stockResponseDtoList.size()) {
			String value = String.format("데이터 사이즈 : %d, page : %d, size : %d", stockResponseDtoList.size(), page, size);
			throw new PagingException(value, ErrorCode.PAGING_ERROR);
		}

		return stockResponseDtoList.subList(startIndex, endIndex);
	}


	public List<StockResponseDto> getPriceChangeRateList() {
//        1. 기준이 될 시각 선정
		LocalDateTime latestDateTime = DateTimeUtil.getLastTradeEndDateTime(LocalDateTime.now());
		LocalDateTime baseDateTime = DateTimeUtil.getLastTradeEndDateTime(latestDateTime.minusDays(1).withHour(23).withMinute(59).withSecond(59));
//        거래 가능일 O, 9시 이전 -> 이전 이전 거래가능일 16일 종가 대비, 이전일 가장 마지막 종가의 비율
//        거래 가능일 X, 16시 이후 -> 이전 이전 거래 가능일 16일 종가 대비, 이전일 가장 마지막 종가의 비율
//		if (!DateTimeUtil.isTradeDate(now) || now.getHour() < 9) {
//			baseDateTime = DateTimeUtil.getLastTradeEndDateTime(baseDateTime.minusDays(1));
//			latestDateTime = DateTimeUtil.getLastTradeEndDateTime(latestDateTime);
//		}

//        2. LocalDateTime을 기준으로 종가 쿼리
		List<StockPriceDto> baseStockPriceList = customStockPriceRepository.findStockLastPrice(baseDateTime);
		List<StockPriceDto> latestStockPriceList = customStockPriceRepository.findStockLastPrice(latestDateTime);

//        stock_id를 key로 세팅하여 비교하기 위해 Map에 세팅
		Map<Integer, StockPriceDto> baseStockPriceMap = baseStockPriceList.stream()
				.collect(Collectors.toMap(StockPriceDto::getStockId, Function.identity()));

		Map<Integer, StockPriceDto> latestStockPriceMap = latestStockPriceList.stream()
				.collect(Collectors.toMap(StockPriceDto::getStockId, Function.identity()));

//        3. 비율 계산
		List<StockPriceChangeDto> stockPriceChangeDtoList = new ArrayList<>();

		baseStockPriceMap.forEach((stockId, stockPriceDto) -> {
			StockPriceDto baseStockPrice = baseStockPriceMap.get(stockId);
			StockPriceDto latestStockPrice = latestStockPriceMap.get(stockId);
			if (baseStockPrice != null && latestStockPrice != null) {
				BigDecimal basePrice = baseStockPrice.getClosePrice();
				BigDecimal latestPrice = latestStockPrice.getClosePrice();
				BigDecimal priceDiff = latestPrice.subtract(basePrice);
				BigDecimal priceRate = priceDiff.divide(basePrice, 4, RoundingMode.HALF_UP);

				StockPriceChangeDto stockPriceChangeDto = StockPriceChangeDto.builder()
						.stockId(stockPriceDto.getStockId())
						.stockCode(stockPriceDto.getStockCode())
						.stockName(stockPriceDto.getStockName())
						.basePrice(basePrice)
						.latestPrice(latestPrice)
						.priceDiff(priceDiff)
						.priceRate(priceRate)
						.build();

				stockPriceChangeDtoList.add(stockPriceChangeDto);
			}
		});

//        4. 정렬해서 Response 만들기
		List<StockResponseDto> stockResponseDtoList = new ArrayList<>();
		stockPriceChangeDtoList.forEach(stockPriceChangeDto -> {
			StockResponseDto stockResponseDto = StockResponseDto.builder()
					.stockId(stockPriceChangeDto.getStockId())
					.stockCode(stockPriceChangeDto.getStockCode())
					.stockName(stockPriceChangeDto.getStockName())
					.price(stockPriceChangeDto.getLatestPrice())
					.priceChangeRate(stockPriceChangeDto.getPriceRate())
					.build();

			stockResponseDtoList.add(stockResponseDto);
		});

		return stockResponseDtoList;
	}

	public List<StockResponseDto> setResponseTotalVolume(List<StockResponseDto> stockResponseDtoList) {
//        1. 기준이 될 시각 선정
//        거래 가능일 O, 9시 이후 -> 당일 거래량 합계
		LocalDateTime localDateTime = LocalDateTime.now();
//        거래 가능일 O, 9시 이전  -> 이전 거래일 거래량 합계
//        거래 가능일 X, 16시 이후 -> 이전 거래일 거래량 합계
		if (!DateTimeUtil.isTradeDate(localDateTime) || localDateTime.getHour() < 9) {
			localDateTime = DateTimeUtil.getLastTradeEndDateTime(localDateTime);
		}
//        거래량 집계 데이터 쿼리
		List<StockTotalVolumeDto> stockTotalVolumeDtoList = customStockPriceRepository.findStockTotalVolume(
				localDateTime);
//        stock_id로 추출을 위해 Map에 세팅
		Map<Integer, StockTotalVolumeDto> stockVolumeDtoMap = stockTotalVolumeDtoList.stream()
				.collect(Collectors.toMap(StockTotalVolumeDto::getStockId, Function.identity()));
//        ResponseDto에 거래량 데이터 세팅
		stockResponseDtoList.forEach(stockResponseDto ->
				stockResponseDto.setTotalVolume(stockVolumeDtoMap.get(stockResponseDto.getStockId()).getTotalVolume()));

		return stockResponseDtoList;
	}

	public List<StockResponseDto> setResponseTotalView(List<StockResponseDto> stockResponseDtoList) {
//        12시간 내의 조회수 집계 데이터 쿼리
		List<StockTotalViewDto> stockTotalViewDtoList = customStockViewRepository.findStockTotalViewAfterDate(
				LocalDateTime.now().minusHours(12));
//        stock_id로 추출을 위해 Map에 세팅
		Map<Integer, StockTotalViewDto> stockViewCountDtoMap = stockTotalViewDtoList.stream()
				.collect(Collectors.toMap(StockTotalViewDto::getStockId, Function.identity()));
//        ResponseDto에 조회수 데이터 세팅
		stockResponseDtoList.forEach(stockResponseDto ->
				stockResponseDto.setTotalView(stockViewCountDtoMap.get(stockResponseDto.getStockId()).getTotalCount()));

		return stockResponseDtoList;
	}

}
