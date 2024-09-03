package org.example.friend.Task;

import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.example.friend.POjO.entity.User;
import org.example.friend.service.ChatService;
import org.example.friend.service.UserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.example.friend.constant.UserConstant.USER_SELECT_KEY;

@Component
@Log4j2
public class ScheduledTask {
    @Resource
    UserService userService;

    @Resource
    ChatService chatService;
    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Scheduled(cron = "0 0 0 * * ?")
    public void preheat() {
        List<User> allUser = userService.getAllUser();
        for (User user : allUser) {
            redisTemplate.opsForValue().set(USER_SELECT_KEY + user.getUsername(), user);
            log.info("数据预热完成");
        }
    }



}
