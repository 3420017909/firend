package org.example.friend.service.imp;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.example.friend.POjO.entity.Chat;
import org.example.friend.POjO.entity.User;
import org.example.friend.mapper.ChatMapper;
import org.example.friend.mapper.UserMapper;
import org.example.friend.request.ExcelRequest;
import org.example.friend.request.MessageRequest;
import org.example.friend.service.ChatService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.example.friend.constant.ChatConstant.CHAT_TEAM_RECOEDS;
import static org.example.friend.constant.ChatConstant.CHAT_USER_RECOEDS;
import static org.example.friend.constant.UserConstant.USER_LOGIN_REQUEST;

@Service
public class ChatServiceimp implements ChatService {

    @Resource
    ChatMapper chatMapper;

    @Resource
    UserMapper userMapper;
    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Resource
    RabbitTemplate rabbitTemplate;
    User user;

    List<MessageRequest> userMessage;
    List<MessageRequest> teamMessage;

    @Override
    public void sendMessage(MessageRequest message, HttpServletRequest request) {
        user = (User) request.getSession().getAttribute(USER_LOGIN_REQUEST);
        message.setFromId(user.getId());
        if (message.getChatType() == 1) {
            rabbitTemplate.convertAndSend("MessageExchange", "userMessage", message);
        } else {
            rabbitTemplate.convertAndSend("MessageExchange", "teamMessage", message);
        }

    }

    @Override
    public void saveUserMessage(MessageRequest message) {
        //先将聊天信息存入缓存，聊天结束后再加入数据库

        userMessage.add(message);
        redisTemplate.opsForList().rightPushAll(CHAT_TEAM_RECOEDS + message.getFromId(), userMessage);


    }

    @Override
    public void saveTeamMessage(MessageRequest message) {
        teamMessage.add(message);
        redisTemplate.opsForList().rightPushAll(CHAT_TEAM_RECOEDS + message.getFromId(), teamMessage);
    }

    @Override
    public void saveMessage() {

        save(userMessage);
        save(teamMessage);
        userMessage = null;
        teamMessage = null;
        redisTemplate.opsForList().trim(CHAT_USER_RECOEDS + user.getId(), 1, 0);
        redisTemplate.opsForList().trim(CHAT_TEAM_RECOEDS + user.getId(), 1, 0);

    }

    @Override
    public List<String> getMessage(long userId, long teamId, HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_REQUEST);

        if (userId!=-1){
            List<String> userMessage = chatMapper.getUserMessage(loginUser.getId(), userId);
            return userMessage;
        }
        List<String> teamMessage = chatMapper.getTeamUser(teamId);
        return teamMessage;
    }

    @Override
    public List<ExcelRequest> exportMessage() {
        List<ExcelRequest> excelRequests = chatMapper.exportMessage();
        return excelRequests;
    }

    public void save(List<MessageRequest> teamMessage) {
        if (teamMessage != null) {
            for (MessageRequest message : teamMessage) {
                Chat chat = new Chat();
                chat.setFromId(message.getFromId());
                chat.setToId(message.getToId());
                chat.setTeamId(message.getTeamId());
                chat.setText(message.getText());
                chat.setChatType(message.getChatType());
                chatMapper.saveChat(chat);
            }

        }
    }
}
