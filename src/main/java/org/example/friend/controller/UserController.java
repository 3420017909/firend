package org.example.friend.controller;



import com.alibaba.excel.EasyExcel;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.friend.POjO.entity.User;
import org.example.friend.request.ExcelRequest;
import org.example.friend.utils.UserControllerUtils;
import org.example.friend.request.UserLoginRequest;
import org.example.friend.response.Response;
import org.example.friend.response.ResponseEntity;
import org.example.friend.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;





    @PostMapping("/register")
    @ApiOperation("注册")
    @ApiImplicitParams({
@ApiImplicitParam(name = "userLoginRequest",value = "手机号、用户名、密码"),
            @ApiImplicitParam(name = "request",value="reqest请求")
    })
    public Response userRegister(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        UserControllerUtils.checkBody(userLoginRequest);
        String token = userService.userRegister(userLoginRequest, request);
        return ResponseEntity.success("注册成功", token);
    }

    @PostMapping("/login")
    public Response userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        UserControllerUtils.checkBody(userLoginRequest);
        String token = userService.userLogin(userLoginRequest, request);
        return ResponseEntity.success("登陆成功", token);
    }

    @PutMapping("/updatepwd")
    public Response userUpdatePwd(@RequestBody UserLoginRequest userLoginRequest) {
        UserControllerUtils.checkBody(userLoginRequest);
        userService.userUpdatePwd(userLoginRequest);
        return ResponseEntity.success("修改密码成功", null);
    }

    @GetMapping("/personage")
    public Response personage(HttpServletRequest request) {
        User user = userService.personage(request);
        return ResponseEntity.success("个人中心", user);
    }
//TODO 有时间再重新测一下，改了下代码，虽然我觉得没问题
    @PutMapping("/updateuser")
    public Response updateUser(@RequestBody User user, HttpServletRequest request) {
        UserControllerUtils.checkByKey(String.valueOf(user));

        userService.updateUser(user, request);
        return ResponseEntity.success("更新成功", null);

    }
//TODO 后面改为直接用String接收json数组，不用先转化为java再转化为json
    @PutMapping("/updatetage")
    public Response updateTag(@RequestBody List<String> tage, HttpServletRequest request) {
        UserControllerUtils.checkByKey(String.valueOf(tage));
        userService.updateTag(tage, request);
        return ResponseEntity.success("更新成功", null);
    }

    @GetMapping("/selectuserbyname")
    public Response selectUser(@RequestParam("username") String userName,
                               @RequestParam(defaultValue = "1") int pageNum,
                               @RequestParam(defaultValue = "10") int pageSize
                               ) {
        UserControllerUtils.checkByKey(userName);
        List<User> users = userService.selectUserByName(userName, pageNum,  pageSize);
        return ResponseEntity.success("搜索成功", users);
    }

    @GetMapping("/selectuserbytage")
    public Response selectUser(@RequestParam("tage") List<String> tage,
                               HttpServletRequest request,
                               @RequestParam(defaultValue = "1") int pageNum,
                               @RequestParam(defaultValue = "10") int pageSize
                               ) {
        UserControllerUtils.checkByKey(tage.toString());
        List<User> users = userService.selectUserByTage(tage,request , pageNum, pageSize);
        return ResponseEntity.success("搜索成功", users);
    }

    @GetMapping("/admin/ban")
    public Response adminBan(@RequestParam long id,HttpServletRequest request) {
UserControllerUtils.checkByKey(String.valueOf(id));
userService.updateStatus(id,request);
return ResponseEntity.success("封禁成功",null);
    }

    @GetMapping("/logout")
    public Response userLogout(HttpServletRequest request) {
        userService.userLogout(request);
        return ResponseEntity.success("退出成功",null);
    }
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        List<ExcelRequest> excelRequests = userService.exportMessage();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=chat.xls");
        response.setCharacterEncoding("utf-8");
        EasyExcel.write(response.getOutputStream(),ExcelRequest.class).sheet("用户信息").doWrite(excelRequests);
    }
}
