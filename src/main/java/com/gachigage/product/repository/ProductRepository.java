package com.gachigage.product.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gachigage.product.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

	@Query("SELECT p FROM Product p "
		+ "JOIN FETCH p.category pc "
		+ "JOIN FETCH p.region pr "
		+ "WHERE pc.id = :categoryId "
		+ "AND pr.province = :province "
		+ "AND pr.city = :city "
		+ "AND p.id != :productId "
		+ "ORDER BY p.createdAt DESC")
	List<Product> findRelatedProducts(
		@Param("categoryId") Long categoryId,
		@Param("province") String province,
		@Param("city") String city,
		@Param("productId") Long productId,
		Pageable pageable);
}
