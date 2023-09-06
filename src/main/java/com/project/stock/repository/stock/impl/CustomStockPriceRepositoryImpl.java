package com.project.stock.repository.stock.impl;

import com.project.stock.model.entity.stock.QStockPrice;
import com.project.stock.model.entity.stock.QSubStockPrice;
import com.project.stock.repository.dto.StockPriceDto;
import com.project.stock.repository.dto.StockTotalVolumeDto;
import com.project.stock.repository.stock.CustomStockPriceRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class CustomStockPriceRepositoryImpl implements CustomStockPriceRepository {

	QStockPrice qStockPrice = QStockPrice.stockPrice;
	QStockPrice qStockPriceSubQuery = new QStockPrice("sub_query");
	QSubStockPrice qSubStockPrice = QSubStockPrice.subStockPrice;
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<StockPriceDto> findAllLatestStockPrice() {

		return jpaQueryFactory
				.select(Projections.constructor(StockPriceDto.class, qStockPrice.stock.id,
						qStockPrice.stock.stockCode, qStockPrice.stock.stockName, qStockPrice.closePrice))
				.from(qStockPrice)
				.join(qSubStockPrice)
				.on(qStockPrice.stock.id.eq(qSubStockPrice.stockId))
				.where(qStockPrice.timestamp.eq(qSubStockPrice.latestTimestamp))
				.fetch();
	}

	@Override
	public List<StockTotalVolumeDto> findStockTotalVolume(LocalDateTime localDateTime) {

		LocalDateTime startDateTime = localDateTime.withHour(9).withMinute(0).withSecond(0).withNano(0);
		LocalDateTime endDateTime = localDateTime.withHour(16).withMinute(0).withSecond(0).withNano(0);

		return jpaQueryFactory
				.select(Projections.constructor(StockTotalVolumeDto.class, qStockPrice.stock.id, qStockPrice.volume.sum()))
				.from(qStockPrice)
				.where(qStockPrice.timestamp.after(startDateTime)
						.and(qStockPrice.timestamp.before(endDateTime)))
				.groupBy(qStockPrice.stock.id)
				.fetch();
	}


	@Override
	public List<StockPriceDto> findStockLastPrice(LocalDateTime localDateTime) {

		LocalDateTime startDateTime = localDateTime.withHour(9).withMinute(0).withSecond(0).withNano(0);

//       조회하려는 시각이 장마감 이후라면 그날의 16시 종가만 보여주면 됨
		if (localDateTime.getHour() >= 16) {
			startDateTime = startDateTime.withHour(15).withMinute(50);
		} else {
//       조회하려는 시각이 장중 이라면 순간의 가격을 확인 하면 됨
			startDateTime = localDateTime.minusMinutes(10);
		}

		return jpaQueryFactory
				.select(Projections.constructor(StockPriceDto.class, qStockPrice.stock.id,
						qStockPrice.stock.stockCode, qStockPrice.stock.stockName, qStockPrice.closePrice))
				.from(qStockPrice)
				.where(qStockPrice.timestamp.eq(
						JPAExpressions.select(qStockPriceSubQuery.timestamp.max())
								.from(qStockPriceSubQuery)
								.where(qStockPriceSubQuery.stock.id.eq(qStockPrice.stock.id)
										.and(qStockPriceSubQuery.timestamp.goe(startDateTime))
										.and(qStockPriceSubQuery.timestamp.loe(localDateTime))
								)
				))
				.fetch();
	}


}
