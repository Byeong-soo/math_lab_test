package com.example.demo.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class ChatRoomMap {
    private static ChatRoomMap chatRoomMap = new ChatRoomMap();
    private Map<String, ChatRoomDTO> chatRooms = new LinkedHashMap<>();

    private ChatRoomMap(){}

    public static ChatRoomMap getInstance() {
        return chatRoomMap;
    }
}
