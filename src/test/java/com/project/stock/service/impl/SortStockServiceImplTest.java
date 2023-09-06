package com.project.stock.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import com.project.stock.common.code.SortField;
import com.project.stock.common.code.SortOrder;
import com.project.stock.common.util.DateTimeUtil;
import com.project.stock.model.dto.response.stock.StockResponseDto;
import com.project.stock.repository.dto.StockPriceDto;
import com.project.stock.repository.dto.StockTotalViewDto;
import com.project.stock.repository.dto.StockTotalVolumeDto;
import com.project.stock.repository.stock.CustomStockPriceRepository;
import com.project.stock.repository.stock.CustomStockViewRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SortStockServiceImplTest {
    @Mock
    private CustomStockViewRepository customStockViewRepository;
    @Mock
    private CustomStockPriceRepository customStockPriceRepository;
    @InjectMocks
    private SortStockServiceImpl sortStockService;

    @Test
    @DisplayName("[Service] 정렬된 전체 데이터 가져오기 테스트")
    void getSortedStockTest() {
//        given
        SortStockServiceImpl spy = Mockito.spy(sortStockService);

        List<StockResponseDto> mockStockResponseList = new ArrayList<>();
        mockStockResponseList.add(StockResponseDto.builder()
            .stockId(1)
            .stockName("카카오페이증권")
            .build());

        doReturn(mockStockResponseList).when(spy).getPriceChangeRateList();
        doReturn(mockStockResponseList).when(spy).setResponseTotalVolume(mockStockResponseList);
        doReturn(mockStockResponseList).when(spy).setResponseTotalView(mockStockResponseList);

//        when
        List<StockResponseDto> result = spy.getSortedStockList(SortField.CHANGE, SortOrder.DESCENDING, 1, 1);

//        then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getOrder());
    }



    @Test
    @DisplayName("[Service] 가격 변화율 데이터 가져오기 테스트")
    void getPriceChangeRateTest() {
//        given
        StockPriceDto mockBasePrice = new StockPriceDto(1, "000001", "카카오페이증권", BigDecimal.valueOf(100));
        List<StockPriceDto> mockBaseStockPriceList = List.of(mockBasePrice);

        StockPriceDto mockLatestPrice = new StockPriceDto(1, "000001", "카카오페이증권", BigDecimal.valueOf(150));
        List<StockPriceDto> mockLatestStockPriceList = List.of(mockLatestPrice);

        LocalDateTime latestDateTime = DateTimeUtil.getLastTradeEndDateTime(LocalDateTime.now());
        LocalDateTime baseDateTime = DateTimeUtil.getLastTradeEndDateTime(latestDateTime.minusDays(1).withHour(23).withMinute(59).withSecond(59));

        when(customStockPriceRepository.findStockLastPrice(baseDateTime)).thenReturn(mockBaseStockPriceList);
        when(customStockPriceRepository.findStockLastPrice(latestDateTime)).thenReturn(mockLatestStockPriceList);

//        when
        List<StockResponseDto> result = sortStockService.getPriceChangeRateList();

//        then
        assertEquals(1, result.size());
        assertThat(BigDecimal.valueOf(0.5000).compareTo(result.get(0).getPriceChangeRate())).isEqualTo(0);
    }

    @Test
    @DisplayName("[Service] 총 거래량 데이터 가져오기 테스트")
    void setResponseTotalVolumeTest() {
//        given
        StockResponseDto mockStockResponseDto = StockResponseDto.builder().stockId(1).build();
        List<StockResponseDto> mockStockResponseDtoList = List.of(mockStockResponseDto);

        StockTotalVolumeDto mockStockTotalVolumeDto = new StockTotalVolumeDto(1, new BigDecimal(1000));

        when(customStockPriceRepository.findStockTotalVolume(any(LocalDateTime.class)))
            .thenReturn(List.of(mockStockTotalVolumeDto));

//        when
        List<StockResponseDto> result = sortStockService.setResponseTotalVolume(mockStockResponseDtoList);

//        then
        assertEquals(1, result.size());
        assertEquals(new BigDecimal(1000), result.get(0).getTotalVolume());
    }

    @Test
    @DisplayName("[Service] 총 조회수 데이터 가져오기 테스트")
    void setResponseTotalViewTest() {
//        given
        StockResponseDto mockStockResponseDto = StockResponseDto.builder().stockId(1).build();
        List<StockResponseDto> mockStockResponseDtoList = List.of(mockStockResponseDto);

        StockTotalViewDto mockStockTotalViewDto = new StockTotalViewDto(1, 1000L);

        when(customStockViewRepository.findStockTotalViewAfterDate(any(LocalDateTime.class)))
            .thenReturn(List.of(mockStockTotalViewDto));

//        when
        List<StockResponseDto> result = sortStockService.setResponseTotalView(mockStockResponseDtoList);

//        then
        assertEquals(1, result.size());
        assertEquals(1000L, result.get(0).getTotalView());
    }
}