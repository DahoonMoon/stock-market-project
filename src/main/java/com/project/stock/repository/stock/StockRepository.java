package com.project.stock.repository.stock;

import com.project.stock.model.entity.stock.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Integer> {

}
