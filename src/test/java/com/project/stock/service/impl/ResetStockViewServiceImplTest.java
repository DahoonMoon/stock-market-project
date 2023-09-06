package com.project.stock.service.impl;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.stock.model.entity.member.Member;
import com.project.stock.model.entity.stock.Stock;
import com.project.stock.repository.member.MemberRepository;
import com.project.stock.repository.stock.StockRepository;
import com.project.stock.repository.stock.StockViewRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ResetStockViewServiceImplTest {
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private StockRepository stockRepository;
    @Mock
    private StockViewRepository stockViewRepository;
    @InjectMocks
    private ResetStockViewServiceImpl resetStockViewService;

    @Test
    @DisplayName("[Service] 주식 조회수 데이터 리셋 테스트")
    void insertNewStockViewTest() {
//        given
        LocalDateTime mockDateTime = LocalDateTime.now();

        List<Member> mockMemberList = new ArrayList<>();
        mockMemberList.add(new Member(1, "a", null));
        mockMemberList.add(new Member(2, "a", null));
        when(memberRepository.findAll()).thenReturn(mockMemberList);

        List<Stock> mockStockList = new ArrayList<>();
        mockStockList.add(new Stock(1, "a", "b", null));
        mockStockList.add(new Stock(2, "a", "b", null));
        when(stockRepository.findAll()).thenReturn(mockStockList);

//        when
        resetStockViewService.createNewStockView(mockDateTime);

//        then
        verify(memberRepository, times(1)).findAll();
        verify(stockRepository, times(1)).findAll();
        verify(stockViewRepository, times(1)).saveAll(anyList());
    }
}