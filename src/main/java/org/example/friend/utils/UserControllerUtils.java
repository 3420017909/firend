package org.example.friend.utils;

import org.apache.commons.lang3.StringUtils;
import org.example.friend.request.UserLoginRequest;
import org.example.friend.exception.CustomException;
import org.springframework.stereotype.Component;

@Component
public class UserControllerUtils {
    //检查用户名和密码是否为空
    public static void checkBody(UserLoginRequest userLoginRequest){
        if (StringUtils.isAnyBlank(userLoginRequest.getUsername(), userLoginRequest.getPassword())) {
            throw new CustomException(4000, "用户名或密码为空");

        }

    }
    public static void checkByKey(String key){
        if (StringUtils.isAnyBlank(key)) {
            throw new CustomException(4004,"内容不能为空");
        }
    }
}
