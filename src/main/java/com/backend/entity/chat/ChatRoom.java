package com.backend.entity.chat;

import com.backend.entity.base.BaseEntity;
import com.backend.entity.meeting.Meeting;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import lombok.*;

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
    public void plusUserCount(Integer count){
        this.userCount += count;
    }

    public void deleteRoomUser(ChatRoomUser chatRoomUser) {
        this.roomUsers.remove(chatRoomUser);
    }
}
