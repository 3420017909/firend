package org.example.friend.service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.friend.request.ExcelRequest;
import org.example.friend.request.MessageRequest;

import java.util.List;

public interface ChatService {

   public void sendMessage(MessageRequest message, HttpServletRequest request) ;

   void saveUserMessage(MessageRequest message);

   void saveTeamMessage(MessageRequest message);

   void saveMessage();

   List<String> getMessage(long userId, long teamId, HttpServletRequest request);

    List<ExcelRequest> exportMessage();
}
