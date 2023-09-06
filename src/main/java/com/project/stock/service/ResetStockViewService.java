package com.project.stock.service;

import java.time.LocalDateTime;

public interface ResetStockViewService {

	void resetStockView();

	void createNewStockView(LocalDateTime localDateTime);

}
