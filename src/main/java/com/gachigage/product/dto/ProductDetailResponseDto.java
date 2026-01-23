package com.gachigage.product.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailResponseDto {
	private Long productId;
	private String title;
	private String detail;
	private String sellerName;
	// For productCategory, GEMINI.md has a nested object. Let's create a nested DTO for it.
	private ProductCategoryDto productCategory; // Nested DTO
	private List<String> tradeTypes; // This was removed from list filter, but is in detail response
	private List<String> imageUrls;
	// For priceTable, GEMINI.md has a nested object. Let's create a nested DTO for it.
	private List<ProductPriceDto> priceTable; // Nested DTO
	// For preferredTradeLocations, GEMINI.md has a nested object. Let's create a nested DTO for it.
	private List<TradeLocationDto> preferredTradeLocations; // Nested DTO
	private Integer viewCount;
	private Integer likeCount;
	private Boolean isLiked;
	// For relatedProducts, GEMINI.md has a nested object. Let's reuse ProductSummaryResponseDto or create a simpler one.
	// Let's create a simpler one, as it has minPrice, maxPrice and thumbnailUrl which is similar to ProductSummaryResponseDto
	private List<RelatedProductDto> relatedProducts; // Nested DTO

	// Nested DTOs for ProductDetailResponseDto
	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ProductCategoryDto {
		private String main;
		private String sub;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ProductPriceDto {
		private Integer minQuantity;
		private Integer price;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TradeLocationDto {
		private Double latitude;
		private Double longitude;
		private String address;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class RelatedProductDto {
		private Long productId;
		private String title;
		private String thumbnailUrl;
		private Integer minPrice;
		private Integer maxPrice;
	}
}
