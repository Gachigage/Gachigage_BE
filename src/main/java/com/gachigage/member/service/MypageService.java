package com.gachigage.member.service;

import com.gachigage.global.error.CustomException;
import com.gachigage.global.error.ErrorCode;
import com.gachigage.image.service.ImageService;
import com.gachigage.member.Member;
import com.gachigage.member.MemberRepository;
import com.gachigage.member.dto.response.MyProfileResponseDto;
import com.gachigage.member.dto.response.TradeResponseDto;
import com.gachigage.product.domain.Product;
import com.gachigage.product.domain.ProductLike;
import com.gachigage.product.repository.ProductLikeRepository;
import com.gachigage.product.repository.ProductRepository;
import com.gachigage.trade.domain.Trade;
import com.gachigage.trade.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MypageService {

    private final MemberRepository memberRepository;
    private final TradeRepository tradeRepository;
    private final ImageService imageService;
private final ProductLikeRepository productLikeRepository;


    public MyProfileResponseDto getMyProfile(Long oauthId){
        Member member = memberRepository.findMemberByOauthId(oauthId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return MyProfileResponseDto.builder()
                .userId(member.getId())
                .name(member.getName())
                .nickname(member.getNickname())
                .profileImage(member.getImageUrl())
                .build();
    }

    @Transactional
    public void updateNickname(Long oauthId, String newNickname) {
        Member member = memberRepository.findMemberByOauthId(oauthId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        member.updateNickname(newNickname);
    }


    @Transactional
    public MyProfileResponseDto updateProfileImage(Long oauthId, MultipartFile file) {
        Member member = memberRepository.findMemberByOauthId(oauthId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 파일이 비어있지 않으면 업로드 진행
        if (file != null && !file.isEmpty()) {
            List<String> imageUrls = imageService.uploadImage(List.of(file));

            String imageUrl = imageUrls.get(0);
            member.updateProfileImage(imageUrl);
        }


        return MyProfileResponseDto.builder()
                .userId(member.getId())
                .name(member.getName())
                .nickname(member.getNickname())
                .profileImage(member.getImageUrl())
                .build();
    }

    public Page<TradeResponseDto> getPurchaseHistory(Long oauthId, Pageable pageable) {
        Member member = memberRepository.findMemberByOauthId(oauthId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return tradeRepository.findAllByBuyerId(member.getId(), pageable)
                .map(this::toTradeResponseDto);
    }

    public Page<TradeResponseDto> getSalesHistory(Long oauthId, Pageable pageable) {
        Member member = memberRepository.findMemberByOauthId(oauthId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return tradeRepository.findAllBySellerId(member.getId(), pageable)
                .map(this::toTradeResponseDto);
    }

    private TradeResponseDto toTradeResponseDto(Trade trade) {
        Product product = trade.getProduct();

        int price = trade.getProductPrice().getPrice();

        String thumbnailUrl = null;
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            thumbnailUrl = product.getImages().get(0).getImageUrl();
        }

        return TradeResponseDto.builder()
                .tradeId(trade.getId())
                .productId(product.getId())
                .title(product.getTitle())
                .price(price)
                .thumbnailUrl(thumbnailUrl)
                .tradeDate(trade.getCreatedAt())
                .status(trade.getStatus())
                .build();
    }
    public Page<TradeResponseDto> getMyLikes(Long oauthId, Pageable pageable) {
        Member member = memberRepository.findMemberByOauthId(oauthId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Page<ProductLike> likes = productLikeRepository.findAllByMemberId(member.getId(), pageable);

        return likes.map(like -> {
            var product = like.getProduct();

            // 1. 대표 이미지 꺼내기 (없으면 null)
            String thumbnailUrl = (product.getImages() != null && !product.getImages().isEmpty())
                    ? product.getImages().get(0).getUrl()
                    : null;

            // 2. 대표 가격 꺼내기 (prices 리스트에서 첫 번째 가격 or 0원)
            // 보통 가격 정책은 1개 가격이 제일 먼저 등록되거나 중요하므로 첫 번째를 가져옵니다.
            int representativePrice = (product.getPrices() != null && !product.getPrices().isEmpty())
                    ? product.getPrices().get(0).getPrice()
                    : 0;

            return TradeResponseDto.builder()
                    .tradeId(null)
                    .productId(product.getId())
                    .title(product.getTitle())
                    .price(representativePrice) // 👈 여기서 뽑아낸 가격을 넣습니다!
                    .thumbnailUrl(thumbnailUrl)
                    .tradeDate(null)
                    .status(String.valueOf(product.getStatus()))
                    .build();
        });
    }
}