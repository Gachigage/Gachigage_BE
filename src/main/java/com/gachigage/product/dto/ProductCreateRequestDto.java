//package com.gachigage.product.dto;
//
//import com.gachigage.product.domain.ProductCategory;
//import com.gachigage.product.domain.PriceTable;
//import com.gachigage.product.domain.Product;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.util.List;
//
//@Getter
//@Setter
//@NoArgsConstructor
//public class ProductCreateRequestDto {
//
//    private ProductCategory productCategory;
//    private String title;
//    private String detail;
//    private List<PriceTable> priceTable;
//    private List<String> tradeTypes;
//    private List<Region> preferredTradeLocations;
//    private List<String> imageUrls;
//
//    public Product toEntity(Long sellerId) {
//        return Product.builder()
//                .sellerId(sellerId)
//                .title(title)
//                .detail(detail)
//                .category(productCategory)
//                .priceTable(priceTable)
//                .tradeTypes(tradeTypes)
//                .preferredTradeLocations(preferredTradeLocations)
//                .imageUrls(imageUrls)
//                .build();
//    }
//}
