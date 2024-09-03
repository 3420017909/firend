package org.example.friend.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.friend.POjO.entity.Team;
import org.example.friend.POjO.entity.UserTeam;

import java.util.List;

/**
* @author 0.0
* @description 针对表【user_team(用户队伍关系)】的数据库操作Mapper
* @createDate 2024-06-10 15:56:43
* @Entity org.example.friend.POjO.entity.UserTeam
*/
@Mapper
public interface UserTeamMapper extends BaseMapper<UserTeam> {
    void addUserTeam(UserTeam userTeam);

    List<String> selectUserTeam(@Param("page") Page<Team> page, @Param("teamid") String teamid);
@Select("select count(user_id) from user_team where team_id=#{teamid} and user_id=#{userid}")
    int selectUser(long teamid,long userid);
@Select("select count(team_id) from user_team where user_id=#{id}")
    int selectTeam(long id);
@Delete("delete from user_team where team_id=#{id}")
    void deleteUserTeam(Long id);
@Delete("delete from user_team where team_id=#{teamId} and user_id=#{userId}")
    void deleteUser(long teamId, long userId);
@Select("select count(user_id) from user_team where team_id=#{teamId}")
    int userCount(long teamId);
}




