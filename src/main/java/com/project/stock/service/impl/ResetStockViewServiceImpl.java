package com.project.stock.service.impl;


import com.project.stock.model.entity.member.Member;
import com.project.stock.model.entity.stock.Stock;
import com.project.stock.model.entity.stock.StockView;
import com.project.stock.repository.member.MemberRepository;
import com.project.stock.repository.stock.StockRepository;
import com.project.stock.repository.stock.StockViewRepository;
import com.project.stock.service.ResetStockViewService;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ResetStockViewServiceImpl implements ResetStockViewService {

	private final MemberRepository memberRepository;
	private final StockRepository stockRepository;
	private final StockViewRepository stockViewRepository;
	Random random = new Random();

	@Override
	public void resetStockView() {

//        기존 데이터 삭제
		stockViewRepository.deleteAllInBatch();

//        새로운 데이터 생성 및 삽입
		createNewStockView(LocalDateTime.now());
	}

	@Override
	public void createNewStockView(LocalDateTime localDateTime) {
		log.info("insertNewStockView Start");
		List<Member> memberList = memberRepository.findAll();
		List<Stock> stockList = stockRepository.findAll();

		LocalDateTime twelveHoursAgo = localDateTime.minusHours(12);

		List<StockView> stockViewList = new ArrayList<>();

		Stream.iterate(twelveHoursAgo, time -> time.plusSeconds(1))
				.limit(Duration.between(twelveHoursAgo, localDateTime).getSeconds())
				.forEach(time -> {
					StockView stockView = StockView.builder()
							.member(memberList.get(random.nextInt(memberList.size())))
							.stock(stockList.get(random.nextInt(stockList.size())))
							.timestamp(time)
							.build();
					stockViewList.add(stockView);
				});

		stockViewRepository.saveAll(stockViewList);

		log.info("insertNewStockView End");
	}

}
