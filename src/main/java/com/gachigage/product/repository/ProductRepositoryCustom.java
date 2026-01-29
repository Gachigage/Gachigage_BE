package com.gachigage.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gachigage.product.dto.ProductListRequestDto;
import com.gachigage.product.dto.ProductListResponseDto;

public interface ProductRepositoryCustom {
	Page<ProductListResponseDto> search(ProductListRequestDto requestDto, Pageable pageable, Long loginMemberId);
}
