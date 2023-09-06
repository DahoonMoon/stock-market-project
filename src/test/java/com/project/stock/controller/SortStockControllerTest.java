package com.project.stock.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.project.stock.common.code.SortField;
import com.project.stock.common.code.SortOrder;
import com.project.stock.model.dto.response.stock.StockResponseDto;
import com.project.stock.service.SortStockService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class SortStockControllerTest {

    @Mock
    private SortStockService sortStockService;
    @InjectMocks
    private SortStockController sortStockController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(sortStockController).build();
    }

    @Test
    @DisplayName("[Controller] Sort Stock API 테스트")
    public void testGetSortedStocks() throws Exception {
        // given
        SortField mockSortField = SortField.CHANGE;
        SortOrder mockSortOrder = SortOrder.DESCENDING;
        int mockPage = 1;
        int mockSize = 1;

        StockResponseDto mockStockResponse = StockResponseDto.builder()
            .order(1)
            .stockId(1)
            .stockCode("123456")
            .stockName("카카오페이증권")
            .price(new BigDecimal(100000))
            .priceChangeRate(new BigDecimal("0.3000"))
            .totalView(10000L)
            .totalVolume(new BigDecimal(1000))
            .build();

        List<StockResponseDto> mockStockList = List.of(mockStockResponse);

        given(sortStockService.getSortedStockList(mockSortField, mockSortOrder, mockPage, mockSize)).willReturn(mockStockList);

        // when
        mockMvc.perform(get("/stocks")
                .param("sortField", mockSortField.name())
                .param("sortOrder", mockSortOrder.name())
                .param("page", String.valueOf(mockPage))
                .param("size", String.valueOf(mockSize))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json("{\n"
                + "  \"status\": 200,\n"
                + "  \"message\": \"OK\",\n"
                + "  \"data\": [{ \"order\": 1,\n"
                + "               \"stockId\": 1,\n"
                + "               \"stockName\": \"카카오페이증권\",\n"
                + "               \"stockCode\": \"123456\",\n"
                + "               \"price\": 100000,\n"
                + "               \"priceChangeRate\": 0.3000,\n"
                + "               \"totalView\": 10000,\n"
                + "               \"totalVolume\": 1000}]}"));

        // then
        then(sortStockService).should().getSortedStockList(mockSortField, mockSortOrder, mockPage, mockSize);
    }

}