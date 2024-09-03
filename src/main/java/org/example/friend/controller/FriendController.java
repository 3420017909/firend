package org.example.friend.controller;



import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.example.friend.request.FriendRequset;
import org.example.friend.POjO.entity.Friends;
import org.example.friend.response.Response;
import org.example.friend.response.ResponseEntity;
import org.example.friend.service.FriendService;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/friend")
public class FriendController {

    @Resource
    FriendService friendService;

    @PostMapping("/add")
    public Response addFriend(@RequestBody FriendRequset friendRequset, HttpServletRequest request) {
        friendService.sendFriendRequest(friendRequset, request);
        return ResponseEntity.success("申请成功", null);
    }

    @GetMapping("/get")
    public Response fromRecords(HttpServletRequest request,
                                @RequestParam(defaultValue = "1") int pageNum,
                                @RequestParam(defaultValue = "10") int pageSize) {
        List<Friends> friends = friendService.fromRecords(request, pageNum,  pageSize);
        return ResponseEntity.success("我申请的列表",friends);
    }

    @GetMapping("/receive")
    public Response receiveRecords(HttpServletRequest request,
                                   @RequestParam(defaultValue = "1") int pageNum,
                                   @RequestParam(defaultValue = "10") int pageSize) {
        List<Friends> friends = friendService.receiveRecords(request,pageNum,pageSize);
        return ResponseEntity.success("我被申请的列表",friends);
    }
    @GetMapping("/agree")
    public Response agree(@RequestParam long id) {
        friendService.agree( id);
        return ResponseEntity.success("已同意",null);
    }
    @GetMapping("/canceled")
    public Response canceled(@RequestParam long id) {
        friendService.canceled( id);
        return ResponseEntity.success("已撤销",null);
    }
}
