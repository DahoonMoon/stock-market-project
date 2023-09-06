package com.project.stock.loader;

import com.project.stock.model.dto.CsvDto;
import com.project.stock.model.entity.member.Member;
import com.project.stock.model.entity.stock.Stock;
import com.project.stock.repository.member.MemberRepository;
import com.project.stock.repository.stock.StockPriceRepository;
import com.project.stock.repository.stock.StockRepository;
import com.project.stock.service.ResetStockPriceService;
import com.project.stock.service.ResetStockViewService;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class InitDataLoader implements CommandLineRunner {

	private final StockRepository stockRepository;
	private final StockPriceRepository stockPriceRepository;
	private final MemberRepository memberRepository;
	private final ResetStockPriceService resetStockPriceService;
	private final ResetStockViewService resetStockViewService;
	private static Faker faker = new Faker();

	@Override
	public void run(String... args) {
		createStockFromCSV();
		createMember();
		createStockPrice();
		createStockView();
	}

	void createStockFromCSV() {
		//		csv파일 읽어와서 종목 데이터 생성
		try {
			String csvFilePath = "init_data/SampleData.csv";

			Reader reader = new InputStreamReader(new ClassPathResource(csvFilePath).getInputStream());
			HeaderColumnNameMappingStrategy<CsvDto> strategy = new HeaderColumnNameMappingStrategy<>();
			strategy.setType(CsvDto.class);

			List<CsvDto> csvDtoList = new CsvToBeanBuilder<CsvDto>(reader)
					.withMappingStrategy(strategy)
					.build()
					.parse();

			List<Stock> stockList = new ArrayList<>();

			csvDtoList.forEach(csvDto -> {
				Stock stock = Stock.builder()
						.stockName(csvDto.getName())
						.stockCode(csvDto.getCode())
						.build();

				stockList.add(stock);
			});

			stockRepository.saveAll(stockList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void createMember() {
		//		멤버 임의로 추가
		List<Member> memberList = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			memberList.add(new Member(faker.name().fullName()));
		}
		memberRepository.saveAll(memberList);
	}

	void createStockPrice() {
		//		종목 정보 마다 랜덤한 가격 데이터 1회 생성
		resetStockPriceService.createNewStockPrice(LocalDateTime.now());
	}

	void createStockView() {
		//		어제 하루 임의의 조회수 데이터 추가
		resetStockViewService.createNewStockView(LocalDateTime.now());
	}


}
