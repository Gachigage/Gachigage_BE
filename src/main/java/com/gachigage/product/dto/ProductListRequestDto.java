package com.gachigage.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductListRequestDto {

	private String q;
	private Long categoryId;
	private Integer minPrice;
	private Integer maxPrice;
	private String province;
	private String city;
	private String district;
	private String group;
	private Integer page;
	private Integer size;
}
