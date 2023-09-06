package com.project.stock.repository.stock.impl;

import static com.project.stock.model.entity.stock.QStockView.stockView;

import com.project.stock.repository.dto.StockTotalViewDto;
import com.project.stock.repository.stock.CustomStockViewRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CustomStockViewRepositoryImpl implements CustomStockViewRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public List<StockTotalViewDto> findStockTotalViewAfterDate(LocalDateTime date) {

		return jpaQueryFactory
				.select(Projections.constructor(StockTotalViewDto.class, stockView.stock.id, stockView.count()))
				.from(stockView)
				.where(stockView.timestamp.goe(date))
				.groupBy(stockView.stock.id)
				.fetch();
	}

}
