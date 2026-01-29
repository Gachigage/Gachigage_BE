package com.gachigage.trade.repository;

import com.gachigage.member.Member;
import com.gachigage.trade.domain.Trade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TradeRepository extends JpaRepository<Trade, Long> {

    Page<Trade> findAllByBuyerId(Long buyerId, Pageable pageable);

    Page<Trade> findAllBySellerId(Long sellerId, Pageable pageable);

}
