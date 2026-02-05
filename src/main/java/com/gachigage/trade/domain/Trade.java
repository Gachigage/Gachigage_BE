package com.gachigage.trade.domain;

import com.gachigage.global.common.BaseEntity;
import com.gachigage.member.Member;
import com.gachigage.product.domain.Product;
import com.gachigage.product.domain.ProductPrice;
import com.gachigage.product.domain.TradeType;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "trade")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Trade extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Member seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private Member buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_price_id", nullable = false)
    private ProductPrice productPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "trade_type", nullable = false)
    private TradeType tradeType;

    @Column(name = "status", nullable = false)
    private String status;
}
