package com.example.demo.service;

import com.example.demo.domain.ChatRoomDTO;
import com.example.demo.domain.ChatRoomMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final MsgChatService msgChatService;
    private final RtcChatService rtcChatService;
    private final BothChatService bothChatService;

    public List<ChatRoomDTO> findAllRoom(){
        List<ChatRoomDTO> chatRooms = new ArrayList<>(ChatRoomMap.getInstance().getChatRooms().values());
        Collections.reverse(chatRooms);

        return chatRooms;
    }

    public ChatRoomDTO findRoomById(String roomId){
        return ChatRoomMap.getInstance().getChatRooms().get(roomId);
    }

    public ChatRoomDTO createChatRoom(String roomName, String chatType) {
        ChatRoomDTO room;

        if(chatType.equals("msgChat")){
            room = msgChatService.createChatRoom(roomName);
        }else if(chatType.equals("rtcChat")){
            room = rtcChatService.createChatRoom(roomName);
        }else{
            room = bothChatService.createChatRoom(roomName);
        }
        return room;
    }

}
