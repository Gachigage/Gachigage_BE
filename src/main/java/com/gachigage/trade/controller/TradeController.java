package com.gachigage.trade.controller;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gachigage.chat.domain.ChatRoom;
import com.gachigage.chat.repository.ChatRoomRepository;
import com.gachigage.global.ApiResponse;
import com.gachigage.global.error.CustomException;
import com.gachigage.global.error.ErrorCode;
import com.gachigage.product.domain.Product;
import com.gachigage.trade.dto.ProductPricesInfoResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/trades")
@RequiredArgsConstructor
public class TradeController {

	private final ChatRoomRepository chatRoomRepository;
	@GetMapping("{chatRoomId}")
	@Transactional
	public ResponseEntity<ApiResponse<ProductPricesInfoResponse>> getProductPricesInfo(@PathVariable Long chatRoomId) {
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "존재하지 않는 채팅방 정보입니다."));

		Product product = chatRoom.getProduct();
		ProductPricesInfoResponse response = new ProductPricesInfoResponse(product.getStock(),product.getPrices());
		return ResponseEntity.ok(ApiResponse.success(response));
	}



}
