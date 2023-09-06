package com.project.stock.controller;

import com.project.stock.model.dto.response.CommonResponse;
import com.project.stock.service.ResetStockPriceService;
import com.project.stock.service.ResetStockViewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Reset Data", description = "데이터 리셋 API 입니다.")
@RequiredArgsConstructor
@RestController
public class ResetDataController {

	private final ResetStockViewService resetStockViewService;
	private final ResetStockPriceService resetStockPriceService;

	@PostMapping("/reset")
	public CommonResponse<String> resetData() {
		resetStockViewService.resetStockView();
		resetStockPriceService.resetStockPrice();
		return new CommonResponse<>(HttpStatus.OK, "데이터 리셋 완료");
	}

}
