package com.gachigage.chat.dto;

import java.time.LocalDateTime;

import com.gachigage.chat.domain.ChatMessage;
import com.gachigage.chat.domain.ChatMessageType;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class ChatMessageResponseDto {

	private final Long chatRoomId;

	private final String content;

	private final Long senderId;

	private final ChatMessageType messageType;

	private final LocalDateTime sendAt;

	private final boolean isRead;

	public static ChatMessageResponseDto from(ChatMessage chatMessage) {
		return ChatMessageResponseDto.builder()
			.chatRoomId(chatMessage.getChatRoom().getId())
			.content(chatMessage.getContent())
			.sendAt(chatMessage.getCreatedAt())
			.senderId(chatMessage.getSender().getOauthId())
			.isRead(chatMessage.isRead())
			.messageType(chatMessage.getMessageType())
			.build();
	}
}
