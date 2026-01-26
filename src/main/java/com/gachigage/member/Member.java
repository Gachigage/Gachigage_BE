package com.gachigage.member;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 50)
	private String name;

	@Column(nullable = false, length = 100, unique = true)
	private String email;

	@Column(length = 50, unique = true)
	private String nickname;

	@Column(name = "birth_date")
	@Temporal(TemporalType.DATE)
	private LocalDate birthDate;

	private byte gender;

	@Column(name = "image_url")
	private String imageUrl;

	@Column(name = "oauth_id")
	private Long oauthId;

	@Column(name = "oauth_provider")
	private String oauthProvider;

	@Enumerated(EnumType.STRING)
	@Column(name = "role_type")
	private RoleType roleType;


	@CreatedDate
	@Column(updatable=false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	@PrePersist
	public void prePersist() {
		LocalDateTime now = LocalDateTime.now();
		this.createdAt = now;
		this.updatedAt = now;
	}


	@PreUpdate
	public void preUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

	public void updateNickname(String nickname) {
		this.nickname = nickname;
	}
}
