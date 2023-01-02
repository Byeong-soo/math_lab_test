package com.example.demo.handler;

import com.example.demo.domain.ChatRoomDTO;
import com.example.demo.domain.ChatRoomMap;
import com.example.demo.domain.WebSocketMessage;
import com.example.demo.service.ChatService;
import com.example.demo.service.RtcChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class SignalHandler extends TextWebSocketHandler {

    private final RtcChatService rtcChatService;
    private final ChatService chatService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Map<String, ChatRoomDTO> rooms = ChatRoomMap.getInstance().getChatRooms();

    private static final String MSG_TYPE_OFFER = "offer";
    private static final String MSG_TYPE_ANSWER = "answer";
    private static final String MSG_TYPE_ICE = "ice";
    private static final String MSG_TYPE_JOIN = "join";
    private static final String MSG_TYPE_LEAVE = "leave";

    private static final String MSG_TYPE_TALK = "talk";

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info("[ws] Session has been closed with status [{} {}]", status, session);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sendMessage(session, new WebSocketMessage("Server", MSG_TYPE_JOIN, Boolean.toString(!rooms.isEmpty()), null, null));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage){
        try {

            WebSocketMessage message = objectMapper.readValue(textMessage.getPayload(),WebSocketMessage.class);
            logger.debug("[ws] Message of {} type from {} received", message.getType(), message.getFrom());

            String userUUID = message.getFrom();
            String roomId = message.getData();

            ChatRoomDTO room;

            switch (message.getType()){

                case MSG_TYPE_OFFER:
                case MSG_TYPE_ANSWER:
                case MSG_TYPE_ICE:
                    Object candidate = message.getCandidate();
                    Object sdp = message.getSdp();

                    logger.debug("[ws] Signal: {}",
                            candidate != null
                                    ? candidate.toString().substring(0, 64)
                                    : sdp.toString().substring(0, 64));

                    ChatRoomDTO roomDto = rooms.get(roomId);

                    if(roomDto != null){
                        Map<String, WebSocketSession> clients = rtcChatService.getClients(roomDto);

                        for (Map.Entry<String, WebSocketSession> client : clients.entrySet()) {
                            if(!client.getKey().equals(userUUID)){
                                sendMessage(client.getValue(),
                                        new WebSocketMessage(
                                                userUUID,
                                                message.getType(),
                                                roomId,
                                                candidate,
                                                sdp
                                        ));
                            }
                        }
                    }
                    break;
                case MSG_TYPE_JOIN:
                    logger.debug("[ws] {} has joined Room: #{}", userUUID, message.getData());
                    room = ChatRoomMap.getInstance().getChatRooms().get(roomId);

                    rtcChatService.addClient(room, userUUID, session);
                    rooms.put(roomId,room);
                    break;
                case MSG_TYPE_LEAVE:
                    logger.info("[ws] {} is going to leave Room: #{}", userUUID, message.getData());
                    room = rooms.get(message.getData());
                    Optional<String> client = rtcChatService.getClients(room).keySet().stream()
                            .filter(clientListKeys -> StringUtils.equals(clientListKeys,userUUID))
                            .findAny();
                    client.ifPresent(userId -> rtcChatService.removeClientByName(room, userId));
                    logger.debug("삭제완료 [{}]", client);
                    break;
                default:
                    logger.debug("[ws] Type of the received message {} is undefined!", message.getType());
            }

        }catch (IOException e){
            logger.debug("An error ocuured: {}",e.getMessage());
        }
    }

    private void sendMessage(WebSocketSession session, WebSocketMessage message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            session.sendMessage(new TextMessage(json));
        } catch (IOException e) {
            logger.debug("An error occured: {}", e.getMessage());
        }
    }
}
