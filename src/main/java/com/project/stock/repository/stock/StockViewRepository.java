package com.project.stock.repository.stock;

import com.project.stock.model.entity.stock.StockView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockViewRepository extends JpaRepository<StockView, Integer> {

}
