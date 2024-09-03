package org.example.friend.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.friend.POjO.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;

import static org.example.friend.constant.UserConstant.USER_LOGIN_REQUEST;

public class CustomInterceptor implements HandlerInterceptor {
@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    String token = request.getHeader("Authorization");
    User user = (User) request.getSession().getAttribute(USER_LOGIN_REQUEST);

    if (token != null && user!=null) {
        return true;
    }else {
        System.out.println("被拦截"+"token"+token+"user"+user);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token.");
        return false;
    }
}
}
