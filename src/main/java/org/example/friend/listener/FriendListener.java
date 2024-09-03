package org.example.friend.listener;

import jakarta.annotation.Resource;
import org.example.friend.request.FriendRequset;
import org.example.friend.request.MessageRequest;
import org.example.friend.service.ChatService;
import org.example.friend.service.FriendService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class FriendListener {

    @Resource
    FriendService friendService;

    @Resource
    ChatService chatService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("friendQueue"),
            exchange = @Exchange("friendExchange"),
            key = "friend"
    ))
    public void receive(FriendRequset friendRequset) {
        friendService.habdleFriendRequest(friendRequset);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("userMessageQueue"),
            exchange = @Exchange("MessageExchange"),
            key = "userMessage"
    ))
    public void sendUserMessage(MessageRequest message) {
        chatService.saveUserMessage(message);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("teamMessageQueue"),
            exchange = @Exchange("MessageExchange"),
            key = "teamMessage"
    ))
    public void sendTeamMessage(MessageRequest message) {
        chatService.saveTeamMessage(message);
    }
}
