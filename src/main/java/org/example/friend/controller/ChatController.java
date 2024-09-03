package org.example.friend.controller;

import com.alibaba.excel.EasyExcel;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.OnClose;
import org.example.friend.POjO.entity.Chat;
import org.example.friend.request.ExcelRequest;
import org.example.friend.request.MessageRequest;
import org.example.friend.response.Response;
import org.example.friend.response.ResponseEntity;
import org.example.friend.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/chat")
public class ChatController {

    @Resource
    ChatService chatService;

    @MessageMapping("send")
    public void sendMessage(MessageRequest message, HttpServletRequest request) {
        chatService.sendMessage(message, request);


    }

    @GetMapping("/getmessage")
    public Response getMessage(@RequestParam("userid") long userId,
                               @RequestParam("teamid") long teamId,
                               HttpServletRequest request) {
        List<String> message = chatService.getMessage(userId, teamId, request);

        return ResponseEntity.success("聊天记录加载完成", message);
    }


    @OnClose
    public Response saveMessage() {
        chatService.saveMessage();
        return ResponseEntity.success("保存聊天记录", null);
    }

}
