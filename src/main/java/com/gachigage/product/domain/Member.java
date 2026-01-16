package com.gachigage.product.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import java.time.LocalDateTime;

// TODO : Entity 관계 설정을 위한 임시 도메인 추후 실제 모델로 대체 예정 - 필드는 임의
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "Member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

    @Column(name = "nickname", length = 12, nullable = false, unique = true)
    private String nickname;

    @Column(name = "birth_date", nullable = false)
    private LocalDateTime birthDate;

    @Column(name = "gender")
    private Byte gender;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "oauth_id", length = 255)
    private String oauthId;

    @Column(name = "oauth_provider", length = 255)
    private String oauthProvider;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
