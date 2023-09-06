package com.project.stock.repository.stock;

import com.project.stock.model.entity.stock.StockPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockPriceRepository extends JpaRepository<StockPrice, Integer> {

}
