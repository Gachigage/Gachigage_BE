package com.gachigage.chat.domain;

import java.time.LocalDateTime;

import com.gachigage.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "chat_message")
public class ChatMessage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "sender_id")
	private Member sender;

	@ManyToOne
	@JoinColumn(name = "chat_room_id")
	private ChatRoom chatRoom;

	@Lob
	private String content;

	@Column(name = "is_read")
	private byte isRead;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "messsage_type")
	private ChatMessageType messageType;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;
}
