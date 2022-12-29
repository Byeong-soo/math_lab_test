package com.example.demo.controller;

import com.example.demo.domain.ChatRoom;
import com.example.demo.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRepository chatRepository;

    @GetMapping("/chat")
    public String goChatRoom(Model model) {
        model.addAttribute("list", chatRepository.findAllRoom());
        log.info("SHOW ALL ChatList {}",chatRepository.findAllRoom());
        return "/chat/roomList";
    }

    @PostMapping("/chat/createroom")
    public String createRoom(@RequestParam String roomName, RedirectAttributes rttr) {

        ChatRoom room = chatRepository.createChatRoom(roomName);
        log.info("CREATE Chat Room{}",room);

        rttr.addFlashAttribute("roomName", room);
        return "redirect:/chat";
    }

    @GetMapping("/chat/room")
    public String roomDetail(Model model, String roomId) {
        log.info("roomId {}", roomId);
        ChatRoom chatRoom = chatRepository.findRoomById(roomId);
        System.out.println("chatRoom.getRoomName() = " + chatRoom.getRoomName());
        System.out.println("chatRoom.getRoomId() = " + chatRoom.getRoomId());
        model.addAttribute("room", chatRoom);
        
        return "/chat/chatroom";
    }

}
