package org.example.friend.service;



import jakarta.servlet.http.HttpServletRequest;
import org.example.friend.request.ExcelRequest;
import org.example.friend.request.UserLoginRequest;
import org.example.friend.POjO.entity.User;

import java.util.List;

public interface UserService {
    String userRegister(UserLoginRequest userLoginRequest, HttpServletRequest request) ;
    String userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request) ;

   void userUpdatePwd(UserLoginRequest userLoginRequest) ;

    List<User> selectUserByName(String userName,int pageNum, int pageSize);

    User personage(HttpServletRequest request);

    void updateUser(User user, HttpServletRequest request);

     void updateTag(List<String> tage, HttpServletRequest request);

    List<User> selectUserByTage(List<String> tage,HttpServletRequest request,int pageNum, int pageSize);

    void updateStatus(long id, HttpServletRequest request);

    void userLogout(HttpServletRequest request);

    List<User> getAllUser();

    List<ExcelRequest> exportMessage();
}
