package org.example.friend.service;


import jakarta.servlet.http.HttpServletRequest;
import org.example.friend.request.TeamAddRequest;
import org.example.friend.request.TeamRequest;
import org.example.friend.POjO.entity.Team;

import java.util.List;

public interface TeamService {
    void addTeam(TeamAddRequest team, HttpServletRequest request);

    void updateTeam(Team team, HttpServletRequest request);

    Team findTeam(String name, HttpServletRequest request);

    List<String> findUserTeam(String teamId, int pageNum, int pageSize);

    void joinTeam(TeamRequest teamRequest, HttpServletRequest request);

    void quitTeam(long id, HttpServletRequest request);

    void deleteTeam(long teamId, HttpServletRequest request);

    void kickUser(long teamId, long userId, HttpServletRequest request);

    int userCount(long teamId);

    List<Team> getAllTeam();
}
