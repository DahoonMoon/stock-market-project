package com.project.stock.service;

import java.time.LocalDateTime;

public interface ResetStockPriceService {

	void resetStockPrice();

	void createNewStockPrice(LocalDateTime localDateTime);

}
