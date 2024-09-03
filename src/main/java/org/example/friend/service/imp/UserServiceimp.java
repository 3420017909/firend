package org.example.friend.service.imp;


import org.example.friend.request.ExcelRequest;
import org.example.friend.utils.AlgorithmUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.friend.POjO.entity.User;
import org.example.friend.request.UserLoginRequest;
import org.example.friend.exception.CustomException;
import org.example.friend.mapper.UserMapper;
import org.example.friend.service.UserService;
import org.example.friend.utils.UserServiceUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.example.friend.constant.UserConstant.*;

@Slf4j
@Service
public class UserServiceimp implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    //TODO 用户注册,没设置手机号或密码问题
    @Override
    public String userRegister(UserLoginRequest userLoginRequest, HttpServletRequest request) {

        UserServiceUtils.checkUser(userLoginRequest.getUsername(), userMapper);

        UserServiceUtils.insertUser(userLoginRequest, userMapper);
        User user = userMapper.getUserByName(userLoginRequest.getUsername());
        User safeUser = UserServiceUtils.getSafeUser(user);
        request.getSession().setAttribute(USER_LOGIN_REQUEST, safeUser);
        request.getSession().setMaxInactiveInterval(86400);
        String userstr = new Gson().toJson(safeUser);
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(USER_LOGIN_KEY + token, userstr, 24, TimeUnit.HOURS);
        return token;

    }

    //用户登录
    @Override
    public String userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request) {
        User user = UserServiceUtils.checkNoUser(userLoginRequest, userMapper);
        String encryptPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());

        if (!encryptPassword.equals(user.getPassword())) {
            throw new CustomException(4003, "用户名或密码错误");
        }
        if (user.getStatus() != 0) {
            throw new CustomException(4004, "你已被封禁");
        }
        User safeUser = UserServiceUtils.getSafeUser(user);
        request.getSession().setAttribute(USER_LOGIN_REQUEST, safeUser);
        request.getSession().setMaxInactiveInterval(86400);
        String userstr = new Gson().toJson(safeUser);
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(USER_LOGIN_KEY + token, userstr, 24, TimeUnit.HOURS);
        return token;
    }

    //忘记密码
    @Override
    public void userUpdatePwd(UserLoginRequest userLoginRequest) {
        UserServiceUtils.checkUser(userLoginRequest.getUsername(), userMapper);
        //TODO 忘记密码需要手机号或密保问题来验证账号的归属
        userMapper.updatePwd(userLoginRequest.getPassword(), userLoginRequest.getUsername());
    }

    //个人中心
    @Override
    public User personage(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_REQUEST);

        if (user == null) {
            throw new CustomException(4006, "系统异常");
        }

        return user;
    }

    //更新用户
    @Override
    public void updateUser(User newUser, HttpServletRequest request) {
        userMapper.updateUser(newUser);

    }

    //更新标签
    @Override
    public void updateTag(List<String> tage, HttpServletRequest request) {
        String newTage = new Gson().toJson(tage);
        User user = (User) request.getSession().getAttribute(USER_LOGIN_REQUEST);


        long id = user.getId();
        userMapper.updateTage(newTage, id);

    }


    @Override
    public List<User> selectUserByName(String userName, int pageNum, int pageSize) {
        List<User> userPage;
        String redisName = (String) redisTemplate.opsForValue().get(USER_SELECT_KEY + userName);
        if (redisName != null) {
            userPage = new Gson().fromJson(redisName, ArrayList.class);
            System.out.println("从redis取出的值" + userPage);

            return userPage;
        }
        Page<User> page = new Page<>(pageNum, pageSize);
        userPage = userMapper.selectUserByName(page, userName);
        if (userPage == null) {
            redisTemplate.opsForValue().set(USER_SELECT_KEY + userName, new User(), 10, TimeUnit.SECONDS);

            throw new CustomException(4006, "无结果");
        }
        String userJson = new Gson().toJson(userPage);
        redisTemplate.opsForValue().set(USER_SELECT_KEY + userName, userJson, 10, TimeUnit.SECONDS);

        return userPage;
    }


    @Override
    public List<User> selectUserByTage(List<String> tage, HttpServletRequest request, int pageNum, int pageSize) {
        List<User> user;
        User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_REQUEST);
        String tageStr = String.join(",", tage);
        String redisTageJson = (String) redisTemplate.opsForValue().get(USER_TAGE_KEY + tageStr);
        if (redisTageJson != null) {
            user = new Gson().fromJson(redisTageJson, new TypeToken<List<User>>() {
            }.getType());
            return user;
        }
        Page<User> page = new Page<>(pageNum, pageSize);
        user = userMapper.selectUserByTage(page, tage);
        if (user == null) {
            redisTemplate.opsForValue().set(USER_TAGE_KEY + tageStr, new User(), 10, TimeUnit.SECONDS);
            throw new CustomException(4006, "无结果");
        }
        List<String> loginTage = new Gson().fromJson(loginUser.getTage(), List.class);

        ArrayList<HashMap<User, Integer>> list = new ArrayList<>();
        HashMap<User, Integer> userMap = new HashMap<>();
        for (User u : user) {
            if (u.getId() != loginUser.getId()) {
                List userTage = new Gson().fromJson(u.getTage(), List.class);
                int distance = AlgorithmUtil.minDistance(loginTage, userTage);
                userMap.put(u, distance);
                list.add(userMap);
            }
        }
        user = list.stream()
                .flatMap(map -> map.entrySet().stream())
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        String userJson = new Gson().toJson(user);
        redisTemplate.opsForValue().set(USER_TAGE_KEY + tageStr, userJson, 10, TimeUnit.SECONDS);
        return user;
    }

    //封禁用户
    @Override
    public void updateStatus(long id, HttpServletRequest request) {
        User admin = (User) request.getSession().getAttribute(USER_LOGIN_REQUEST);
        Integer adminRole = admin.getRole();
        if (adminRole != 1) {
            throw new CustomException(4007, "无权限");
        }
        User user = userMapper.selectUserById(id);
        if (user.getStatus() == 0) {
            userMapper.updateStatus(user.getId());
        }
    }

    //用户退出
    @Override
    public void userLogout(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_REQUEST);
        String token = request.getHeader("Authorization");
        redisTemplate.delete(USER_LOGIN_KEY + token);
        request.getSession().removeAttribute(USER_LOGIN_REQUEST);
    }

    @Override
    public List<User> getAllUser() {
        List<User> allUser = userMapper.getAllUser();
        return allUser;
    }

    @Override
    public List<ExcelRequest> exportMessage() {
        List<ExcelRequest> excelRequests = userMapper.exportUser();
        return excelRequests;
    }


}
