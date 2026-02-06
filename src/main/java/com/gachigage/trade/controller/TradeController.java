package com.gachigage.trade.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gachigage.global.ApiResponse;
import com.gachigage.trade.domain.Trade;
import com.gachigage.trade.dto.ProductPricesInfoResponse;
import com.gachigage.trade.dto.TradeRequest;
import com.gachigage.trade.service.TradeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/trades")
@RequiredArgsConstructor
public class TradeController {

	private final TradeService tradeService;

	@GetMapping("{chatRoomId}")
	@Transactional
	public ResponseEntity<ApiResponse<ProductPricesInfoResponse>> getProductPricesInfo(@PathVariable Long chatRoomId) {
		ProductPricesInfoResponse response = tradeService.getProductInfo(chatRoomId);
		return ResponseEntity.ok(ApiResponse.success(response));
	}

	@PostMapping("/{chatRoomId}")
	public ResponseEntity<ApiResponse> registerTradeInfo(@PathVariable Long chatRoomId, TradeRequest request) {
		tradeService.createTrade(chatRoomId, request.getProductPriceId());
		return ResponseEntity.ok(ApiResponse.success());
	}

}
