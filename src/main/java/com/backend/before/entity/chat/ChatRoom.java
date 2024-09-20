package com.backend.before.entity.chat;

import com.backend.before.entity.base.BaseEntity;
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
@Table(name = "chat_rooms")
@Getter
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    private String chatRoomName;

    private String uuid;

    @Builder.Default
    private Integer userCount = 0;

    @Builder.Default
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ChatMessage> messages = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ChatRoomUser> roomUsers = new ArrayList<>();

    public void addMessage(ChatMessage message) {
        this.messages.add(message);
    }

    public void addRoomUser(ChatRoomUser roomUser) {
        this.roomUsers.add(roomUser);
        plusUserCount(1);
    }

    public void plusUserCount(Integer count) {
        this.userCount += count;
    }

    public void deleteRoomUser(ChatRoomUser chatRoomUser) {
        this.roomUsers.remove(chatRoomUser);
    }

    public void updateChatRoomName(String title) {
        this.chatRoomName = title;
    }
}
