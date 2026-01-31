package com.gachigage.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gachigage.product.dto.ProductListRequestDto;
import com.gachigage.product.dto.ProductListResponseDto;

import com.gachigage.product.domain.Product;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface ProductRepositoryCustom {
	Page<ProductListResponseDto> search(ProductListRequestDto requestDto, Pageable pageable, Long loginMemberId);

	List<Product> findRelatedProducts(
		@Param("categoryId") Long categoryId,
		@Param("province") String province,
		@Param("city") String city,
		@Param("productId") Long productId,
		Pageable pageable);
}
