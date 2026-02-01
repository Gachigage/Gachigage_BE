package com.gachigage.chat.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gachigage.chat.dto.ChatRoomCreateRequestDto;
import com.gachigage.chat.dto.ChatRoomCreateResponseDto;
import com.gachigage.chat.dto.ChatRoomResponseDto;
import com.gachigage.chat.service.ChatService;
import com.gachigage.global.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RequestMapping("/chats")
@RestController
@RequiredArgsConstructor
public class ChatHttpController {
	private final ChatService chatService;

	@Operation(summary = "채팅방 생성/입장", description = "상품 ID를 받아 채팅방을 생성하거나, 이미 존재하면 해당 방 번호를 반환합니다.")
	@PostMapping
	public ResponseEntity<ApiResponse<ChatRoomCreateResponseDto>> createOrGetChatRoom(
		@RequestBody ChatRoomCreateRequestDto requestDto,
		@Parameter(hidden = true) @AuthenticationPrincipal User user) {
		return ResponseEntity.ok(
			ApiResponse.success(chatService.createOrFindRoom(requestDto, Long.parseLong(user.getUsername()))));
	}

	@GetMapping("/rooms")
	public ResponseEntity<ApiResponse<List<ChatRoomResponseDto>>> getMyRooms(@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(ApiResponse.success(chatService.getMyChatRooms(Long.parseLong(user.getUsername()))));
	}
}
