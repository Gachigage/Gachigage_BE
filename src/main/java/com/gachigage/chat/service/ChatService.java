package com.gachigage.chat.service;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.gachigage.chat.domain.ChatMessageType;
import com.gachigage.chat.domain.ChatRoom;
import com.gachigage.chat.dto.ChatMessageDto;
import com.gachigage.chat.dto.ChatRoomCreateRequestDto;
import com.gachigage.chat.repository.ChatRoomRepository;
import com.gachigage.global.error.CustomException;
import com.gachigage.global.error.ErrorCode;
import com.gachigage.member.Member;
import com.gachigage.member.MemberRepository;
import com.gachigage.product.domain.Product;
import com.gachigage.product.repository.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

	private final SimpMessagingTemplate template;
	private final ChatRoomRepository chatRoomRepository;
	private final MemberRepository memberRepository;
	private final ProductRepository productRepository;

	public Long createOrFindRoom(ChatRoomCreateRequestDto requestDto,
		Long buyerOauthId) {
		Long productId = requestDto.getProductId();
		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
		Member seller = product.getSeller();
		Member buyer = memberRepository.findMemberByOauthId(buyerOauthId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		if (Objects.equals(buyer.getOauthId(), seller.getOauthId())) {
			throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "자신의 상품에는 채팅을 걸 수 없습니다.");
		}

		return chatRoomRepository.findByProductAndBuyer(product, buyer)
			.map(ChatRoom::getId)
			.orElseGet(() -> {
				ChatRoom chatRoom = ChatRoom.builder()
					.buyer(buyer)
					.seller(seller)
					.product(product)
					.lastMessage("")
					.lastMessageTime(LocalDateTime.now())
					.sellerLastReadMessageId(0L)
					.buyerLastReadMessageId(0L)
					.build();
				chatRoomRepository.save(chatRoom);

				return chatRoom.getId();
			});
	}

	public void processMessage(ChatMessageDto messageDto) {
		if (ChatMessageType.ENTER.equals(messageDto.getMessageType())) {
			messageDto.setMessage(messageDto.getSenderNickname() + "님이 입장하셨습니다.");
		}

		template.convertAndSend("/sub/chat/room/" + messageDto.getChatRoomId(), messageDto);
	}
}
