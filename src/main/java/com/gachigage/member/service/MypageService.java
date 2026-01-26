package com.gachigage.member.service;


import com.gachigage.member.Member;
import com.gachigage.member.MemberRepository;
import com.gachigage.member.dto.response.MyProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MypageService {

    private final MemberRepository memberRepository;


    public MyProfileResponseDto getMyProfile(Long memberId){

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return MyProfileResponseDto.builder()
                .userId(member.getId())
                .name(member.getName())
                .nickname(member.getNickname())
                .profileImage(member.getImageUrl())
                .build();
    }

}
