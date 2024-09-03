package org.example.friend.utils;


import com.google.gson.Gson;
import org.example.friend.request.UserLoginRequest;
import org.example.friend.POjO.entity.User;
import org.example.friend.exception.CustomException;
import org.example.friend.mapper.UserMapper;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;

public class UserServiceUtils {


    //判断账号是否存在
    public static void checkUser(String username, UserMapper userMapper) throws CustomException {
        Integer userCount = userMapper.userCountByName(username);
        if (userCount > 0) {
            throw new CustomException(4001, "账号已存在");
        }
    }

    //新增用户
    public static void insertUser(UserLoginRequest userLoginRequest, UserMapper userMapper)  {
        String encryptPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());
        User user = new User();
        user.setPhone(userLoginRequest.getPhone());
        user.setUsername(userLoginRequest.getUsername());
        user.setPassword(encryptPassword);

        ArrayList<String> list = new ArrayList<>();
        String tage = new Gson().toJson(list);
        user.setTage(tage);
        userMapper.insertUser(user);
    }

    //返回安全对象
    public static User getSafeUser(User user) {
        User safeUser = new User();
        safeUser.setId(user.getId());
        safeUser.setPhone(user.getPhone());
        safeUser.setUsername(user.getUsername());
        safeUser.setProfile(user.getProfile());
        safeUser.setGender(user.getGender());
        safeUser.setStatus(user.getStatus());
        safeUser.setRole(user.getRole());
        safeUser.setTage(user.getTage());
        return safeUser;

    }
    //判断用户是否不存在
    public static User checkNoUser(UserLoginRequest userLoginRequest, UserMapper userMapper) {
        User user = userMapper.getUserByName(userLoginRequest.getUsername());
        if (user.getUsername() == null) {
            throw new CustomException(4002, "账号不存在");
        }

        return user;
    }
}
