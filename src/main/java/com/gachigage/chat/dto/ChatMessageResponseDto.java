package com.gachigage.chat.dto;

import java.time.LocalDateTime;

import com.gachigage.chat.domain.ChatMessage;
import com.gachigage.chat.domain.ChatMessageType;
import com.gachigage.chat.domain.ChatRoom;
import com.gachigage.member.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class ChatMessageResponseDto {

	private final Long chatRoomId;

	private final String content;

	private final boolean senderIsBuyer;

	private final ChatMessageType messageType;

	private final LocalDateTime sendAt;

	private final boolean isRead;

	public static ChatMessageResponseDto from(ChatMessage chatMessage) {
		ChatRoom chatRoom = chatMessage.getChatRoom();
		Member sender = chatMessage.getSender();
		return ChatMessageResponseDto.builder()
			.chatRoomId(chatMessage.getChatRoom().getId())
			.content(chatMessage.getContent())
			.sendAt(chatMessage.getCreatedAt())
			.senderIsBuyer(chatRoom.getBuyer().equals(sender))
			.messageType(chatMessage.getMessageType())
			.build();
	}
}
