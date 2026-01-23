package com.gachigage.product.service;

import static com.gachigage.global.error.ErrorCode.RESOURCE_NOT_FOUND;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gachigage.global.error.CustomException;
import com.gachigage.member.Member;
import com.gachigage.member.MemberRepository;
import com.gachigage.product.domain.Product;
import com.gachigage.product.domain.ProductCategory;
import com.gachigage.product.domain.ProductImage;
import com.gachigage.product.domain.ProductPrice;
import com.gachigage.product.domain.Region;
import com.gachigage.product.domain.TradeType;
import com.gachigage.product.dto.ProductRegistrationRequestDto;
import com.gachigage.product.repository.ProductCategoryRepository;
import com.gachigage.product.repository.ProductRepository;
import com.gachigage.product.repository.RegionRepository;

@Service
public class ProductService {

	private final ProductCategoryRepository productCategoryRepository;
	private final ProductRepository productRepository;
	private final MemberRepository memberRepository;

	private final RegionRepository regionRepository;

	public ProductService(ProductCategoryRepository productCategoryRepository,
			ProductRepository productRepository,
			MemberRepository memberRepository,
			RegionRepository regionRepository) {
		this.productCategoryRepository = productCategoryRepository;
		this.productRepository = productRepository;
		this.memberRepository = memberRepository;
		this.regionRepository = regionRepository;
	}

	@Transactional
	public Product createProduct(
			Long loginMemberId,
			Long subCategoryId,
			String title,
			String detail,
			Long stock,
			List<ProductRegistrationRequestDto.ProductPriceRegistrationDto> priceTable,
			TradeType tradeType,
			ProductRegistrationRequestDto.TradeLocationRegistrationDto preferredTradeLocation,
			List<String> imageUrls) {

		ProductCategory category = productCategoryRepository.findById(subCategoryId)
				.orElseThrow(() -> new CustomException(RESOURCE_NOT_FOUND, "존재하지 않는 카테고리입니다"));

		Member seller = memberRepository.findById(loginMemberId)
				.orElseThrow(() -> new CustomException(RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다"));

		// Todo : 시도동으로 외부 API 연동 후 region 설정 _ 이미 존재하는 region인지 확인 필요
		Region region = regionRepository.save(new Region("서울특별시", "강남구", "역삼동")); // dummy data

		Product product = Product.builder()
				.seller(seller)
				.category(category)
				.region(region)
				.title(title)
				.description(detail)
				.stock(stock)
				.tradeType(tradeType)
				.latitude(preferredTradeLocation.getLatitude())
				.longtitude(preferredTradeLocation.getLongitude())
				.build();

		priceTable.stream()
				.map(priceDto -> ProductPrice.builder()
						.quantity(priceDto.getQuantity())
						.price(priceDto.getPrice())
						.build())
				.forEach(product::addPrice);

		imageUrls.stream()
				.map(imageUrl -> ProductImage.builder()
						.imageUrl(imageUrl)
						.build())
				.forEach(product::addImage);

		productRepository.save(product);
		return product;
	}
}
