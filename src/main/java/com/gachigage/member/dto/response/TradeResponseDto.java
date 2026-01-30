package com.gachigage.member.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class TradeResponseDto {
    private Long tradeId;
    private Long productId;
    private String title;
    private int price;
    private String thumbnailUrl;
    private LocalDateTime tradeDate;
    private String status;
}
