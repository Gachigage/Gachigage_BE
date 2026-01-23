package com.gachigage.product.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gachigage.global.login.CustomOAuth2User;
import com.gachigage.product.domain.Product;
import com.gachigage.product.dto.ProductRegistrationRequestDto;
import com.gachigage.product.dto.ProductRegistrationResponseDto;
import com.gachigage.product.service.ProductService;


@RestController
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@PostMapping
	public ResponseEntity<ProductRegistrationResponseDto> registerProduct(
			@RequestBody ProductRegistrationRequestDto requestDto,
			@AuthenticationPrincipal CustomOAuth2User user) {
		Product product = productService.createProduct(
				user.getMember().getId(),
				requestDto.getProductCategory().getSubCategoryId(),
				requestDto.getTitle(),
				requestDto.getDetail(),
				requestDto.getStock(),
				requestDto.getPriceTable(),
				requestDto.getTradeType(),
				requestDto.getPreferredTradeLocations(),
				requestDto.getImageUrls()
		);
		return ResponseEntity.ok(ProductRegistrationResponseDto.builder().productId(product.getId())
				.message("상품이 성공적으로 등록되었습니다.").build());
	}
}
