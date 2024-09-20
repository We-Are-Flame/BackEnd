package com.backend.before.entity.user;

import com.backend.before.entity.chat.ChatRoomUser;
import com.backend.meeting.domain.meeting.entity.Meeting;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "setting_id")
    private Setting setting;

    private String nickname;

    private String profileImage;

    @Builder.Default
    private Integer temperature = 365;

    private String email;

    private String schoolEmail;

    @OneToMany(mappedBy = "host", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Meeting> meetings;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ChatRoomUser> chatRoomUsers = new ArrayList<>();

    public Boolean getIsSchoolVerified() { return this.schoolEmail != null; }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public boolean isSameId(User otherUser) {
        return this.id.equals(otherUser.getId());
    }

    public void updateNotification(Boolean isNotification) {
        this.setting.updateNotification(isNotification);
    }

    public void addChatUser(ChatRoomUser roomUser) {
        this.chatRoomUsers.add(roomUser);
    }

    public void deleteRoomUser(ChatRoomUser roomUser) {
        this.chatRoomUsers.remove(roomUser);
    }

    public void updateSchoolEmail(String schoolEmail) {
        this.schoolEmail = schoolEmail;
    }

    public void updateTemperature(int newTemperature) {
        this.temperature = newTemperature;
    }
}
