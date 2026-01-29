package com.gachigage.product.repository;

import static com.gachigage.product.domain.QProduct.product;
import static com.gachigage.product.domain.QProductImage.productImage;
import static com.gachigage.product.domain.QProductPrice.productPrice;
import static com.gachigage.product.domain.QRegion.region;
import static com.gachigage.product.domain.QProductLike.productLike;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.gachigage.product.dto.ProductListRequestDto;
import com.gachigage.product.dto.ProductListResponseDto;
import com.gachigage.product.dto.QProductListResponseDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import com.gachigage.product.domain.ProductCategory;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

	private final JPAQueryFactory queryFactory;
	private final ProductCategoryRepository productCategoryRepository;

	@Override
	public Page<ProductListResponseDto> search(ProductListRequestDto requestDto, Pageable pageable, Long loginMemberId) {
		List<ProductListResponseDto> content = queryFactory
			.select(new QProductListResponseDto(
				product.id,
				product.title,
				productImage.imageUrl,
				region.province,
				region.city,
				new CaseBuilder()
					.when(productPrice.quantity.goe(2)).then("일괄")
					.otherwise("개별"),
				product.tradeType,
				productPrice.price,
				productPrice.quantity,
				product.visitCount,
				new CaseBuilder()
					.when(loginMemberId != null ?
						JPAExpressions
							.selectOne()
							.from(productLike)
							.where(productLike.product.eq(product).and(productLike.member.oauthId.eq(loginMemberId)))
							.exists()
						:
						Expressions.asBoolean(false).isTrue() // Always returns a BooleanExpression (false)
					)
					.then(true)
					.otherwise(false),
				product.createdAt
			))
			.from(productPrice)
			.join(productPrice.product, product)
			.leftJoin(product.images, productImage).on(productImage.order.eq(0))
			.leftJoin(product.region, region)
			.where(
				titleContains(requestDto.query()),
				categoryEq(requestDto.categoryId()),
				priceBetween(requestDto.priceArrange()),
				locationEq(requestDto.locationDto()),
				groupEq(requestDto.group())
			)
			.orderBy(product.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = queryFactory
			.select(productPrice.count())
			.from(productPrice)
			.join(productPrice.product, product)
			.leftJoin(product.region, region)
			.where(
				titleContains(requestDto.query()),
				categoryEq(requestDto.categoryId()),
				priceBetween(requestDto.priceArrange()),
				locationEq(requestDto.locationDto()),
				groupEq(requestDto.group())
			)
			.fetchOne();

		return new PageImpl<>(content, pageable, total);
	}

	private BooleanExpression titleContains(String query) {
		return query != null ? product.title.containsIgnoreCase(query) : null;
	}

	private BooleanExpression categoryEq(Long categoryId) {
		if (categoryId == null) {
			return null;
		}

		ProductCategory category = productCategoryRepository.findById(categoryId).orElse(null);
		if (category == null) {
			return null;
		}

		if (category.getParent() == null) { // This is a main category
			Set<Long> descendantCategoryIds = new HashSet<>();
			descendantCategoryIds.add(categoryId); // Include the main category itself
			getAllDescendantCategoryIds(categoryId, descendantCategoryIds);
			return product.category.id.in(descendantCategoryIds);
		} else { // This is a subcategory
			return product.category.id.eq(categoryId);
		}
	}

	private void getAllDescendantCategoryIds(Long parentId, Set<Long> descendantCategoryIds) {
		List<ProductCategory> children = productCategoryRepository.findByParentId(parentId);
		for (ProductCategory child : children) {
			descendantCategoryIds.add(child.getId());
			getAllDescendantCategoryIds(child.getId(), descendantCategoryIds);
		}
	}

	private BooleanExpression priceBetween(ProductListRequestDto.PriceArrangeDto priceArrange) {
		if (priceArrange == null) {
			return null;
		}
		return productPrice.price.between(priceArrange.minPrice(), priceArrange.maxPrice());
	}

	private BooleanExpression locationEq(ProductListRequestDto.LocationDto locationDto) {
		if (locationDto == null) {
			return null;
		}
		return region.province.eq(locationDto.province())
			.and(region.city.eq(locationDto.city()));
	}

	private BooleanExpression groupEq(String group) {
		if (group == null || group.equals("전체")) {
			return null;
		} else if (group.equals("일괄")) {
			return productPrice.quantity.goe(2);
		} else if (group.equals("개별")) {
			return productPrice.quantity.eq(1);
		}
		return null;
	}
}
