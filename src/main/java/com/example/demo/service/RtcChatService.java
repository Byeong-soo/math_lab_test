package com.example.demo.service;

import com.example.demo.domain.ChatRoomDTO;
import com.example.demo.domain.ChatRoomMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class RtcChatService {

    public ChatRoomDTO createChatRoom(String roomName){

        ChatRoomDTO room = ChatRoomDTO.builder()
                .roomId(UUID.randomUUID().toString())
                .roomName(roomName)
                .build();

        room.setVideoUserList(new HashMap<String, WebSocketSession>());
        room.setChatType(ChatRoomDTO.ChatType.RTC);
        ChatRoomMap.getInstance().getChatRooms().put(room.getRoomId(), room);

        return room;
    }

    public Map<String ,WebSocketSession> getClients(ChatRoomDTO room){
        Optional<ChatRoomDTO> roomDto = Optional.ofNullable(room);

        return roomDto.get().getVideoUserList();
    }

    public Map<String, WebSocketSession> addClient(ChatRoomDTO room, String name, WebSocketSession session) {
        Map<String, WebSocketSession> userList = room.getVideoUserList();
        userList.put(name, session);
        return userList;
    }

    public void removeClientByName(ChatRoomDTO room, String userUUID) {
        room.getVideoUserList().remove(userUUID);
    }

}
