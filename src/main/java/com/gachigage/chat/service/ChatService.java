package com.gachigage.chat.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.gachigage.chat.domain.ChatMessageType;
import com.gachigage.chat.dto.ChatMessageDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {

	private final SimpMessagingTemplate template;

	public void processMessage(ChatMessageDto messageDto) {
		if (ChatMessageType.ENTER.equals(messageDto.getMessageType())) {
			messageDto.setMessage(messageDto.getSenderNickname() + "님이 입장하셨습니다.");
		}
		
		template.convertAndSend("/sub/chat/room/" + messageDto.getChatRoomId(), messageDto);
	}
}
