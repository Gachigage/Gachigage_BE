package com.gachigage.member.service;

import com.gachigage.global.error.CustomException;
import com.gachigage.global.error.ErrorCode;
import com.gachigage.image.service.ImageService;
import com.gachigage.member.Member;
import com.gachigage.member.MemberRepository;
import com.gachigage.member.dto.response.MyProfileResponseDto;
import com.gachigage.member.dto.response.ProfileImageResponseDto;
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
                .email(member.getEmail())
                .createdAt(member.getCreatedAt())
                .build();
    }

    @Transactional
    public void updateNickname(Long oauthId, String newNickname) {
        Member member = memberRepository.findMemberByOauthId(oauthId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        member.updateNickname(newNickname);
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


            String thumbnailUrl = (product.getImages() != null && !product.getImages().isEmpty())
                    ? product.getImages().get(0).getImageUrl()
                    : null;


            int representativePrice = (product.getPrices() != null && !product.getPrices().isEmpty())
                    ? product.getPrices().get(0).getPrice()
                    : 0;

            return TradeResponseDto.builder()
                    .tradeId(null)
                    .productId(product.getId())
                    .title(product.getTitle())
                    .price(representativePrice)
                    .thumbnailUrl(thumbnailUrl)
                    .tradeDate(null)
                    .status(String.valueOf(product.getStatus()))
                    .build();
        });
    }
    @Transactional
    public ProfileImageResponseDto updateProfileImage(Long oauthId, MultipartFile file) {
        Member member = memberRepository.findMemberByOauthId(oauthId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));


        String imageUrl = imageService.uploadImage(List.of(file)).get(0);



        member.updateProfileImage(imageUrl);

        return ProfileImageResponseDto.builder()
                .imageUrl(imageUrl)
                .build();
    }
}