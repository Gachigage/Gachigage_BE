package com.gachigage.member.controller;


import com.gachigage.global.ApiResponse;
import com.gachigage.member.dto.response.ProfileImageResponseDto;
import com.gachigage.member.dto.response.SellerProfileResponseDto;
import com.gachigage.member.dto.response.TradeResponseDto;
import com.gachigage.member.service.MemberService;
import com.gachigage.member.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
private final MypageService mypageService;

    @GetMapping("/{memberId}/profile")
    public ApiResponse<SellerProfileResponseDto> getSellerProfile(@PathVariable Long memberId) {
        SellerProfileResponseDto response = memberService.getSellerProfile(memberId);
        return ApiResponse.success(response);
    }
    @GetMapping("/{memberId}/products")
    public ApiResponse<Page<TradeResponseDto>> getSellerProducts(
            @PathVariable Long memberId,
            @RequestParam(required = false) String status,
            Pageable pageable) {

        Page<TradeResponseDto> response = memberService.getSellerProducts(memberId, status, pageable);
        return ApiResponse.success(response);
    }

    @PostMapping("/user/image")
    public ApiResponse<ProfileImageResponseDto> registerProfileImage(
            @AuthenticationPrincipal UserDetails user,
            @RequestPart("file") MultipartFile file) {

        Long oauthId = Long.valueOf(user.getUsername());
        return ApiResponse.success(mypageService.updateProfileImage(oauthId, file));

    }

}
