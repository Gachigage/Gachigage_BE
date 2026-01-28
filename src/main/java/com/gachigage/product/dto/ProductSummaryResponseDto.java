package com.gachigage.product.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSummaryResponseDto {
	private Long productId;
	private String title;
	private Integer minPrice;
	private Integer maxPrice;
	private String thumbnailUrl;
	private String productCategory;
	private String province;
	private String city;
	private String district;
	private Integer viewCount;
	private Boolean isLiked;
	private LocalDateTime createdAt;
}
