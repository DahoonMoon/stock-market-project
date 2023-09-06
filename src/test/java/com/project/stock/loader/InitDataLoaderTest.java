package com.project.stock.loader;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.project.stock.repository.member.MemberRepository;
import com.project.stock.repository.stock.StockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InitDataLoaderTest {

	@InjectMocks
	InitDataLoader initDataLoader;
	@Mock
	StockRepository stockRepository;
	@Mock
	MemberRepository memberRepository;

	@Test
	@DisplayName("[Loader] init data run 테스트")
	void runTest(){
//		given
		InitDataLoader initDataLoaderSpy = Mockito.spy(initDataLoader);

		doNothing().when(initDataLoaderSpy).createStockView();
		doNothing().when(initDataLoaderSpy).createStockPrice();
		doNothing().when(initDataLoaderSpy).createMember();

//		when
		initDataLoaderSpy.run();

//		then
		verify(initDataLoaderSpy, times(1)).createStockFromCSV();
	}


	@Test
	@DisplayName("[Loader] Stock 임의 데이터 생성 테스트")
	void createStockTest() {
//		given
//		when
		initDataLoader.createStockFromCSV();

//		then
		verify(stockRepository, times(1)).saveAll(anyList());
	}

	@Test
	@DisplayName("[Loader] Member 임의 데이터 생성 테스트")
	void createMemberTest() {
//		given
//		when
		initDataLoader.createMember();

//		then
		verify(memberRepository, times(1)).saveAll(anyList());
	}


}