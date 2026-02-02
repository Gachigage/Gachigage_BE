package com.gachigage.chat.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.gachigage.chat.domain.ChatMessage;
import com.gachigage.chat.domain.ChatMessageType;
import com.gachigage.chat.domain.ChatRoom;
import com.gachigage.chat.dto.ChatMessageRequestDto;
import com.gachigage.chat.dto.ChatMessageResponseDto;
import com.gachigage.chat.dto.ChatRoomCreateRequestDto;
import com.gachigage.chat.dto.ChatRoomCreateResponseDto;
import com.gachigage.chat.dto.ChatRoomResponseDto;
import com.gachigage.chat.repository.ChatMessageRepository;
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
	private final ChatMessageRepository chatMessageRepository;

	public ChatRoomCreateResponseDto createOrFindRoom(ChatRoomCreateRequestDto requestDto, Long buyerOauthId) {
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
			.map(chatRoom -> ChatRoomCreateResponseDto.builder()
				.chatRoomId(chatRoom.getId())
				.productId(chatRoom.getProduct().getId())
				.sellerNickname(chatRoom.getSeller().getNickname())
				.sellerImageUrl(chatRoom.getSeller().getImageUrl())
				.buyerNickname(chatRoom.getBuyer().getNickname())
				.buyerImageUrl(chatRoom.getBuyer().getImageUrl())
				.build())
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

				return ChatRoomCreateResponseDto.builder()
					.chatRoomId(chatRoom.getId())
					.productId(chatRoom.getProduct().getId())
					.sellerNickname(chatRoom.getSeller().getNickname())
					.sellerImageUrl(chatRoom.getSeller().getImageUrl())
					.buyerNickname(chatRoom.getBuyer().getNickname())
					.buyerImageUrl(chatRoom.getBuyer().getImageUrl())
					.build();
			});
	}

	public List<ChatRoomResponseDto> getMyChatRooms(Long userOauthId) {
		if (userOauthId == null) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);
		}

		List<ChatRoom> rooms = chatRoomRepository.findMyChatRooms(userOauthId);

		return rooms.stream().map(room -> {
			boolean isMyRoleSeller = userOauthId.equals(room.getSeller().getOauthId());
			Member otherMember = isMyRoleSeller ? room.getBuyer() : room.getSeller();

			Long myLastReadId = isMyRoleSeller ? room.getSellerLastReadMessageId() : room.getBuyerLastReadMessageId();
			int unreadCount = chatMessageRepository.countUnreadMessages(room.getId(), myLastReadId);

			return ChatRoomResponseDto.builder()
				.chatRoomId(room.getId())
				.otherName(otherMember.getNickname())
				.otherProfileImage(otherMember.getImageUrl())
				.lastMessage(room.getLastMessage())
				.lastMessageTime(room.getLastMessageTime())
				.unreadCount(unreadCount)
				.build();
		}).toList();
	}

	public Slice<ChatMessageResponseDto> getChatMessages(Long chatRoomId, Pageable pageable, Long userOauthId) {
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

		if (!chatRoom.getBuyer().getOauthId().equals(userOauthId) && !chatRoom.getSeller()
			.getOauthId()
			.equals(userOauthId)) {
			throw new CustomException(ErrorCode.ACCESS_DENIED, "이 채팅방에 접근 권한이 없습니다.");
		}

		Slice<ChatMessage> messages = chatMessageRepository.findByChatRoomIdOrderByCreatedAtDesc(chatRoomId,
			pageable);

		return messages.map(ChatMessageResponseDto::from);
	}

	public void processMessage(ChatMessageRequestDto messageRequestDto, Long memberOauthId) {
		ChatRoom chatRoom = chatRoomRepository.findById(messageRequestDto.getChatRoomId())
			.orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

		Member sender = memberRepository.findMemberByOauthId(memberOauthId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		ChatMessage chatMessage = ChatMessage.builder()
			.chatRoom(chatRoom)
			.sender(sender)
			.messageType(ChatMessageType.TEXT)
			.content(messageRequestDto.getContent())
			.createdAt(LocalDateTime.now())
			.build();

		chatMessageRepository.save(chatMessage);
		chatRoom.updateLastMessage(chatMessage);
		chatRoom.updateLastReadMessageId(sender.getOauthId(), chatMessage.getId());

		ChatMessageResponseDto responseDto = ChatMessageResponseDto.from(chatMessage);

		template.convertAndSend("/sub/chat/room/" + chatRoom.getId(), responseDto);
	}

	public boolean isMemberInRoom(Long memberOauthId, Long chatRoomId) {
		Member member = memberRepository.findMemberByOauthId(memberOauthId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

		return chatRoom.getBuyer() == member || chatRoom.getSeller() == member;
	}
}
