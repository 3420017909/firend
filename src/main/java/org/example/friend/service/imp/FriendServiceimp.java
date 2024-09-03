package org.example.friend.service.imp;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.example.friend.request.FriendRequset;
import org.example.friend.POjO.entity.Friends;
import org.example.friend.POjO.entity.User;
import org.example.friend.exception.CustomException;
import org.example.friend.mapper.FriendsMapper;
import org.example.friend.mapper.UserMapper;
import org.example.friend.service.FriendService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.example.friend.constant.FriendConstant.FRIEND_FROM_RECORD;
import static org.example.friend.constant.FriendConstant.FRIEND_RECEIVE_RECORD;
import static org.example.friend.constant.UserConstant.USER_LOGIN_REQUEST;

@Service
public class FriendServiceimp implements FriendService {
    @Resource
    FriendsMapper friendsMapper;

    @Resource
    UserMapper userMapper;

    @Resource
    RabbitTemplate rabbitTemplate;

    @Resource
    RedisTemplate redisTemplate;

    @Override
    public void sendFriendRequest(FriendRequset friendRequset, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_REQUEST);
        Integer userCount = userMapper.userCount(friendRequset.getReceiveid());
        if (userCount == 0) {
            throw new CustomException(4006, "用户不存在");
        }
        if (user.getId() == friendRequset.getReceiveid()) {
            throw new CustomException(4200, "不能添加自己为好友");
        }
        friendRequset.setFromid(user.getId());
        rabbitTemplate.convertAndSend("friendExchange", "friend", friendRequset);
    }

    @Override
    public void habdleFriendRequest(FriendRequset friendRequset) {
        Friends friends = new Friends();
        friends.setFromId(friendRequset.getFromid());
        friends.setReceiveId(friendRequset.getReceiveid());
        friends.setCreateTime(new Date());
        friends.setRemark(friendRequset.getRemake());
        friendsMapper.addFriends(friends);
    }

    @Override
    public List<Friends> fromRecords(HttpServletRequest request,int pageNum, int pageSize) {
        List<Friends> friends;
        User user = (User) request.getSession().getAttribute(USER_LOGIN_REQUEST);
        String friendStr = (String) redisTemplate.opsForValue().get(FRIEND_FROM_RECORD + user.getId());
        if (friendStr != null) {
             friends = new Gson().fromJson(friendStr, ArrayList.class);
            return friends;
        }
        Page<Friends> page = new Page<>(pageNum, pageSize);
        friends = friendsMapper.fromRecords(page,user.getId());
        String friendsJson = new Gson().toJson(friends);
        redisTemplate.opsForValue().set(FRIEND_FROM_RECORD + user.getId(), friendsJson, 10, TimeUnit.SECONDS);
        return friends;
    }

    @Override
    public List<Friends> receiveRecords(HttpServletRequest request,int pageNum, int pageSize) {
        List<Friends> friends;
        User user = (User) request.getSession().getAttribute(USER_LOGIN_REQUEST);
        String friendStr = (String) redisTemplate.opsForValue().get(FRIEND_RECEIVE_RECORD + user.getId());
        if (friendStr != null) {
             friends = new Gson().fromJson(friendStr, ArrayList.class);
return friends;
        }
        Page<Friends> page = new Page<>(pageNum, pageSize);
         friends = friendsMapper.receiveRecords(page, user.getId());
        String friendsJson = new Gson().toJson(friends);
        redisTemplate.opsForValue().set(FRIEND_RECEIVE_RECORD + user.getId(), friendsJson, 10, TimeUnit.SECONDS);
return friends;
    }

    @Override
    public void agree( long id) {

        Integer status=1;
        friendsMapper.updateRecord(status, id);
    }

    @Override
    public void canceled( long id) {

        Integer status=3;
        friendsMapper.updatefrom(status, id);
    }
    }
