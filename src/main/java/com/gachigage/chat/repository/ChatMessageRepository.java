package com.gachigage.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gachigage.chat.domain.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
	@Query("select count(m) from ChatMessage m where m.chatRoom.id = :chatRoomId and m.id > :lastReadId")
	int countUnreadMessages(@Param("chatRoomId") Long chatRoomId, @Param("lastReadId") Long lastReadId);
}
