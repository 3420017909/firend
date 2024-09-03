package org.example.friend.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.friend.POjO.entity.Chat;
import org.example.friend.POjO.entity.User;
import org.example.friend.request.ExcelRequest;

import java.util.List;

/**
* @author 0.0
* @description 针对表【chat(聊天消息表)】的数据库操作Mapper
* @createDate 2024-06-15 16:48:50
* @Entity org.example.friend.POjO.entity.Chat
*/
public interface ChatMapper {



    void saveChat(Chat chat);
@Select("select chat.text from chat where from_id=#{loginUserId} and to_id=#{userId}")
    List<String> getUserMessage(@Param("loginUserId") long loginUserId, @Param("userId") long userId);
@Select("select chat.text from chat where team_id=#{teamId}")
    List<String> getTeamUser(long teamId);
@Select("select chat.from_id,chat.to_id,chat.team_id,chat.chat_type,chat.text from chat")
    List<ExcelRequest> exportMessage();
}




