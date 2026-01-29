package com.gachigage.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.gachigage.chat.dto.ChatMessageDto;
import com.gachigage.chat.service.ChatService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatStompController {

	private final ChatService chatService;

	@MessageMapping("/chat/message")
	public void message(ChatMessageDto messageDto) {
		chatService.processMessage(messageDto);
	}
}
