package com.backend.dto.chat.request.create;

import com.backend.entity.chat.MessageType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatCreateRequest {

    @JsonProperty("message_type")
    private MessageType messageType;
    @JsonProperty("room_id")
    private String roomId;
    private String sender;
    @JsonProperty("sender_id")
    private Long senderId;
    private String message;
    public void updateMessage(String message){
        this.message = message;
    }

}
