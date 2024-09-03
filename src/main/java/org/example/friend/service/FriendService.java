package org.example.friend.service;


import jakarta.servlet.http.HttpServletRequest;
import org.example.friend.request.FriendRequset;
import org.example.friend.POjO.entity.Friends;

import java.util.List;

public interface FriendService {
    void sendFriendRequest(FriendRequset friendRequset, HttpServletRequest request);

    void habdleFriendRequest(FriendRequset friendRequset);

    List<Friends> fromRecords(HttpServletRequest request, int pageNum, int pageSize);

    List<Friends> receiveRecords(HttpServletRequest request,int pageNum, int pageSize);

    void agree(long id);

    void canceled(long id);

}
