package com.example.demo.domain;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;

@Data
@Builder
public class ChatRoomDTO {

    @NotNull
    private String roomId;
    private String roomName;
    private long userCount;

    public enum ChatType{
        MSG,RTC,BOTH
    }

    private ChatType chatType;

    private HashMap<String, String > textUserList;
    private HashMap<String, WebSocketSession> videoUserList;
}
