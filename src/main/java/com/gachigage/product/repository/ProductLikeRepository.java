package com.gachigage.product.repository;

import com.gachigage.product.domain.ProductLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductLikeRepository extends JpaRepository<ProductLike,Long> {
}
