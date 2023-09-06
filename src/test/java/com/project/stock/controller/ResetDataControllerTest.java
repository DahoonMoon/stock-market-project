package com.project.stock.controller;

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.project.stock.service.ResetStockPriceService;
import com.project.stock.service.ResetStockViewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class ResetDataControllerTest {

    @Mock
    private ResetStockViewService resetStockViewService;
    @Mock
    private ResetStockPriceService resetStockPriceService;
    @InjectMocks
    private ResetDataController resetDataController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(resetDataController).build();
    }

    @Test
    @DisplayName("[Controller] Reset Data API 테스트")
    public void testResetData() throws Exception {
        // given
        willDoNothing().given(resetStockViewService).resetStockView();
        willDoNothing().given(resetStockPriceService).resetStockPrice();

        // when
        mockMvc.perform(post("/reset")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        // then
        then(resetStockViewService).should().resetStockView();
        then(resetStockPriceService).should().resetStockPrice();
    }

}