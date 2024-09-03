package org.example.friend.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.friend.POjO.entity.Team;
import org.example.friend.request.TeamAddRequest;

import java.util.List;

/**
* @author 0.0
* @description 针对表【team】的数据库操作Mapper
* @createDate 2024-06-10 15:58:09
* @Entity org.example.friend.POjO.entity.Team
*/
@Mapper
public interface TeamMapper {
@Select("select count(*) from team where name=#{name}")
    Integer teamCount(String name);

    void addTeam(TeamAddRequest addTeam);

    void updateTeam(Team team);
@Select("select * from team where id=#{id}")
    Team selectTeam(Long id);
@Select("select * from team where name=#{name}")
    Team selectTeamByName(String name);
@Select("select count(*) from team where id=#{id}")
    int userConut(long id);
@Delete("delete from team where id=#{teamId}")
    void deleteTeam(long teamId);
@Select("select count(*) from team where id=#{teamId}")
    Integer teamCountById(long teamId);
@Select("select * from team")
    List<Team> getAllTeam();
}




