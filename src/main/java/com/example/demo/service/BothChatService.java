package com.example.demo.service;

import com.example.demo.domain.ChatRoomDTO;
import com.example.demo.domain.ChatRoomMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.lang.reflect.Array;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class BothChatService {

    public ChatRoomDTO createChatRoom(String roomName){

        ChatRoomDTO room = ChatRoomDTO.builder()
                .roomId(UUID.randomUUID().toString())
                .roomName(roomName)
                .build();

//        ChatRoomDTO chatRoom = ChatRoomDTO.builder()
//                .roomId(UUID.randomUUID().toString())
//                .roomName(roomName)
//                .build();

//        room.setUserList(new HashMap<String,ArrayList<Object>>());
        room.setVideoUserList(new HashMap<String, WebSocketSession>());
        room.setTextUserList(new HashMap<String ,String>());
//        chatRoom.setUserList(new HashMap<String, String>());

        room.setChatType(ChatRoomDTO.ChatType.BOTH);
        ChatRoomMap.getInstance().getChatRooms().put(room.getRoomId(), room);
        return room;
    }






//    public Map<String ,ArrayList<Object>> getClients(ChatRoomDTO room){
//        Optional<ChatRoomDTO> roomDto = Optional.ofNullable(room);
//
//        return (Map<String, ArrayList<Object>>) roomDto.get().getUserList();
//    }
//
//    public Map<String, ArrayList<Object>> addClient(ChatRoomDTO room, String name, WebSocketSession session) {
//        Map<String, ArrayList<Object>> userList = (Map<String, ArrayList<Object>>) room.getUserList();
//        String userUUID = UUID.randomUUID().toString();
//
//        ArrayList<Object> user = new ArrayList<>();
//        user.add(0,name);
//        user.add(1,session);
//        userList.put(userUUID, user);
//        return userList;
//    }
//
//    public void removeClientByName(ChatRoomDTO room, String userUUID) {
//        room.getUserList().remove(userUUID);
//    }
}
