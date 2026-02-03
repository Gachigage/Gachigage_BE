package com.gachigage.product.dto;

import jakarta.annotation.Nullable;

public record ProductListRequestDto(

	@Nullable
	String query,

	@Nullable
	Long categoryId,

	@Nullable
	PriceArrangeDto priceArrange,
	@Nullable
	LocationDto locationDto,
	@Nullable
	String group,
	Integer page,
	Integer size
) {

	public record PriceArrangeDto(
		Integer minPrice,
		Integer maxPrice
	) {
	}

	public record LocationDto(
		String province,
		String city
	) {
	}
}
