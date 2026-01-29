package com.gachigage.chat.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import com.gachigage.member.Member;
import com.gachigage.product.domain.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "chatroom")
public class ChatRoom {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "sender_id")
	private Member sender;

	@ManyToOne
	@JoinColumn(name = "buyer_id")
	private Member buyer;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@CreatedDate
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;
}
