package com.gachigage.member.controller;

import com.gachigage.global.ApiResponse;
import com.gachigage.member.dto.request.NicknameUpdateRequestDto;
import com.gachigage.member.dto.response.MyProfileResponseDto;
import com.gachigage.member.dto.response.ProfileImageResponseDto;
import com.gachigage.member.dto.response.TradeResponseDto;
import com.gachigage.member.service.MypageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users/me")
@RequiredArgsConstructor
public class MypageController {
    private final MypageService mypageService;


    @GetMapping
    public ApiResponse<MyProfileResponseDto> getMyProfile(@AuthenticationPrincipal UserDetails user) {
        Long oauthId = Long.valueOf(user.getUsername());
        MyProfileResponseDto response = mypageService.getMyProfile(oauthId);


        return ApiResponse.success(response);
    }

    @PutMapping("/nickname")
    public ApiResponse<Void> updateNickname(@AuthenticationPrincipal UserDetails user,
                                            @Valid @RequestBody NicknameUpdateRequestDto request) {
        Long oauthId = Long.valueOf(user.getUsername());
        mypageService.updateNickname(oauthId, request.getNickname());


        return ApiResponse.success(null);
    }


    @PutMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ProfileImageResponseDto> updateProfileImage(@AuthenticationPrincipal UserDetails user,
                                                                @RequestPart(value = "file") MultipartFile file) {

        Long oauthId = Long.valueOf(user.getUsername());


        ProfileImageResponseDto response = mypageService.updateProfileImage(oauthId, file);

        return ApiResponse.success(response);
    }


    @GetMapping("/purchases")
    public ApiResponse<Page<TradeResponseDto>> getPurchaseHistory(
            @AuthenticationPrincipal UserDetails user,
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {


        Long oauthId = Long.valueOf(user.getUsername());


        Page<TradeResponseDto> response = mypageService.getPurchaseHistory(oauthId, pageable);

        return ApiResponse.success(response);
    }


    @GetMapping("/sales")
    public ApiResponse<Page<TradeResponseDto>> getSalesHistory(
            @AuthenticationPrincipal UserDetails user,
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Long oauthId = Long.valueOf(user.getUsername());

        Page<TradeResponseDto> response = mypageService.getSalesHistory(oauthId, pageable);

        return ApiResponse.success(response);
    }


}
