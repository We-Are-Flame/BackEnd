package com.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

    @OneToOne
    @JoinColumn(name="setting_id")
    private Setting setting;

    private String nickname;

    private String profileImage;

    private Integer temperature;
}
