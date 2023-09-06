package com.project.stock.repository.stock.impl;


import static org.assertj.core.api.Assertions.assertThat;

import com.project.stock.config.TestConfig;
import com.project.stock.model.entity.member.Member;
import com.project.stock.model.entity.stock.Stock;
import com.project.stock.model.entity.stock.StockView;
import com.project.stock.repository.dto.StockTotalViewDto;
import com.project.stock.repository.member.MemberRepository;
import com.project.stock.repository.stock.CustomStockViewRepository;
import com.project.stock.repository.stock.StockRepository;
import com.project.stock.repository.stock.StockViewRepository;
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
@Import({TestConfig.class, CustomStockViewRepositoryImpl.class})
class CustomStockViewRepositoryImplTest {

	@Autowired
	private CustomStockViewRepository customStockViewRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private MemberRepository memberRepository;
	@Autowired
	private StockViewRepository stockViewRepository;

	LocalDateTime mockDateTime = LocalDateTime.of(2023, 6, 20, 12, 0, 0);

	@BeforeEach
	void createTest() {
        Stock mockStock = Stock.builder()
                .stockCode("000001")
                .stockName("test")
                .build();

        Member mockMember = Member.builder()
                .userName("tester")
                .build();

        StockView mockStockView = StockView.builder()
                .stock(mockStock)
                .member(mockMember)
                .timestamp(mockDateTime)
                .build();

        stockRepository.save(mockStock);
        memberRepository.save(mockMember);
        stockViewRepository.save(mockStockView);

	}

	@Test
	@DisplayName("[QueryDSL] 특정 DateTime 이후 stock_id 별 조회수 총합 테스트")
	void findStockTotalViewAfterDateTest() {

        List<StockTotalViewDto> result = customStockViewRepository.findStockTotalViewAfterDate(mockDateTime);

		assertThat(result.size() > 0).isTrue();

	}
}