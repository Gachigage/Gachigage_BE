package com.gachigage.product.dto;

import java.util.List;

import com.gachigage.product.domain.TradeType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRegistrationRequestDto {
    private ProductCategoryRegistrationDto productCategory; // Changed name and type of nested DTO
    private String title;
    private String detail;
    private Long stock;
    private List<ProductPriceRegistrationDto> priceTable;
    private TradeType tradeType;
    private TradeLocationRegistrationDto preferredTradeLocations; // Changed from List to single object
    private List<String> imageUrls;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductCategoryRegistrationDto {
        private Long mainCategoryId;
        private Long subCategoryId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductPriceRegistrationDto {
        private Integer quantity;
        private Integer price;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TradeLocationRegistrationDto {
        private Double latitude;
        private Double longitude;
        private String address;
    }
}
