package com.gachigage.member.controller;

import com.gachigage.member.dto.request.NicknameUpdateRequestDto;
import com.gachigage.member.dto.response.MyProfileResponseDto;
import com.gachigage.member.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/me")
@RequiredArgsConstructor
public class MypageController {
    private final MypageService mypageService;


    @GetMapping
    public ResponseEntity<MyProfileResponseDto> getMyProfile(@AuthenticationPrincipal UserDetails user) {
        Long oauthId = Long.valueOf(user.getUsername()); // 의미가 더 명확해짐
        MyProfileResponseDto response = mypageService.getMyProfile(oauthId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/nickname")
    public ResponseEntity<Void> updateNickname(@AuthenticationPrincipal UserDetails user,
                                               @RequestBody NicknameUpdateRequestDto request) {

        Long oauthId = Long.valueOf(user.getUsername());

        mypageService.updateNickname(oauthId, request.getNickname());

        return ResponseEntity.ok().build();
    }

}
