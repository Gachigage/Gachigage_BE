package com.gachigage.member.controller;

import com.gachigage.member.dto.response.MyProfileResponseDto;
import com.gachigage.member.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user/me")
@RequiredArgsConstructor
public class MypageController {
    private final MypageService mypageService;


    @GetMapping
    public ResponseEntity<MyProfileResponseDto> getMyProfile(@AuthenticationPrincipal UserDetails user) {
        Long memberId = Long.valueOf(user.getUsername());
        MyProfileResponseDto response = mypageService.getMyProfile(memberId);
        return ResponseEntity.ok(response);
    }


}
