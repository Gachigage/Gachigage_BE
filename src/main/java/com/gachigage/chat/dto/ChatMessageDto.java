package com.gachigage.chat.dto;

import com.gachigage.chat.domain.ChatMessageType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class ChatMessageDto {

	private Long chatRoomId;

	private String senderNickname;

	private String message;

	private ChatMessageType messageType;

	private String sendTime;
}
