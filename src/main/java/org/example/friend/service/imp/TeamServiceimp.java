package org.example.friend.service.imp;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.example.friend.mapper.UserMapper;
import org.example.friend.request.TeamAddRequest;
import org.example.friend.request.TeamRequest;
import org.example.friend.POjO.entity.Team;
import org.example.friend.POjO.entity.User;
import org.example.friend.POjO.entity.UserTeam;
import org.example.friend.exception.CustomException;
import org.example.friend.mapper.TeamMapper;
import org.example.friend.mapper.UserTeamMapper;
import org.example.friend.service.TeamService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.example.friend.constant.TeamConstant.TEAM_EXIST;
import static org.example.friend.constant.TeamConstant.TEAM_USER;
import static org.example.friend.constant.UserConstant.USER_LOGIN_REQUEST;

@Service
public class TeamServiceimp implements TeamService {

    @Resource
    TeamMapper teamMapper;

    @Resource
    UserMapper userMapper;
    @Resource
    UserTeamMapper userTeamMapper;
    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public void addTeam(TeamAddRequest addTeam, HttpServletRequest request) {
        //TODO,还需要加锁（分布式锁）
        Integer teamCount = teamMapper.teamCount(addTeam.getName());
        if (teamCount > 0) {
            throw new CustomException(4100, "队伍已存在");
        }
        User user = (User) request.getSession().getAttribute(USER_LOGIN_REQUEST);
        addTeam.setUserId(user.getId());
        addTeam.setCreateTime(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(addTeam.getCreateTime());
        calendar.add(Calendar.DATE, addTeam.getIntervalDate());
        Date expirationTime = calendar.getTime();
        addTeam.setExpireTime(expirationTime);
        teamMapper.addTeam(addTeam);
        teamCount = teamMapper.teamCount(addTeam.getName());
        if (teamCount == 0) {
            throw new CustomException(4101, "队伍创建失败");
        }
        System.out.println("team表已创建");
        Team team = teamMapper.selectTeamByName(addTeam.getName());
        UserTeam userTeam = new UserTeam();
        userTeam.setTeamId(team.getId());
        userTeam.setUserId(user.getId());
        userTeam.setJoinTime(new Date());
        userTeamMapper.addUserTeam(userTeam);

    }

    public void updateTeam(Team team, HttpServletRequest request) {
        Team findTeam = teamMapper.selectTeam(team.getId());
        User user = (User) request.getSession().getAttribute(USER_LOGIN_REQUEST);

        if (findTeam == null) {
            throw new CustomException(4102, "队伍不存在");
        }
        if (!findTeam.getUserId().equals(user.getId()) && !user.getRole().equals(1)) {
            throw new CustomException(4007, "无权限");
        }
        if (findTeam.getStatus() != 2 && team.getPassword().equals(findTeam.getPassword())) {
            throw new CustomException(4102, "队伍密码错误");
        }

        teamMapper.updateTeam(team);


    }

    @Override
    public Team findTeam(String name, HttpServletRequest request) {
        Team team;
        team = (Team) redisTemplate.opsForValue().get(TEAM_EXIST + name);
        if (team != null) {
            return team;
        }
        team = teamMapper.selectTeamByName(name);
        if (team == null && new Date().after(team.getExpireTime())) {
            redisTemplate.opsForValue().set(TEAM_EXIST + name, new Team(), 10, TimeUnit.SECONDS);
            throw new CustomException(4006, "无结果");
        }
        redisTemplate.opsForValue().set(TEAM_EXIST + name, team, 10, TimeUnit.SECONDS);
        return team;
    }

    @Override
    public List<String> findUserTeam(String teamId, int pageNum, int pageSize) {
        List<String> userList;
        String userListStr = (String) redisTemplate.opsForValue().get(TEAM_USER + teamId);
        if (userListStr != null) {
            userList = new Gson().fromJson(userListStr, ArrayList.class);
            return userList;
        }
        Page<Team> page = new Page<>(pageNum, pageSize);

        userList = userTeamMapper.selectUserTeam(page, teamId);

        String userJson = new Gson().toJson(userList);
        redisTemplate.opsForValue().set(TEAM_USER + teamId, userJson, 10, TimeUnit.SECONDS);
        return userList;
    }

    @Override
    public void joinTeam(TeamRequest teamRequest, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_REQUEST);
        Team team = teamMapper.selectTeam(teamRequest.getId());
        if (team.getExpireTime() != null && new Date().after(team.getExpireTime())) {
            throw new CustomException(4104, "队伍已经过期");
        }
        if (team.getStatus() == 1) {
            throw new CustomException(4105, "禁止加入私有队伍");
        }
        if (team.getStatus() == 2 && !team.getPassword().equals(teamRequest.getPassword())) {
            throw new CustomException(4106, "队伍密码错误");
        }
        int userConut = teamMapper.userConut(teamRequest.getId());
        if (userConut > team.getMaxNum()) {
            throw new CustomException(4108, "队伍已经满了");
        }
        int teamCount = userTeamMapper.selectTeam(user.getId());
        if (teamCount > 5) {
            throw new CustomException(4109, "最多加入5个队伍");
        }
        int userCount = userTeamMapper.selectUser(team.getId(), user.getId());
        if (userCount > 0) {
            throw new CustomException(4107, "禁止重复加入");
        }

        UserTeam userTeam = new UserTeam();
        userTeam.setTeamId(teamRequest.getId());
        userTeam.setUserId(user.getId());
        userTeam.setJoinTime(new Date());
        userTeamMapper.addUserTeam(userTeam);
    }

    @Override
    public void quitTeam(long teamId, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_REQUEST);

        int userCount = userTeamMapper.selectUser(teamId, user.getId());
        if (userCount == 0) {
            throw new CustomException(4110, "未加入该队伍");
        }
        userTeamMapper.deleteUserTeam(teamId);
    }

    @Override
    public void deleteTeam(long teamId, HttpServletRequest request) {
        Integer teamCount = teamMapper.teamCountById(teamId);
        if (teamCount == 0) {
            throw new CustomException(4102, "队伍不存在");
        }
        userTeamMapper.deleteUserTeam(teamId);
        teamMapper.deleteTeam(teamId);
    }

    @Override
    public void kickUser(long teamId, long userId, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_REQUEST);
        Team team = teamMapper.selectTeam(teamId);
        int userCount = userTeamMapper.selectUser(teamId, userId);
        if (userCount == 0) {
            throw new CustomException(4111, "队伍中不存在该用户");
        }
        if (team.getUserId().equals(user.getId())  && !user.getRole().equals(1)) {
            throw new CustomException(4007, "无权限");
        }
        userTeamMapper.deleteUser(teamId, userId);
    }

    @Override
    public int userCount(long teamId) {
        int userCount = userTeamMapper.userCount(teamId);
        return userCount;
    }

    @Override
    public List<Team> getAllTeam() {
        List<Team> allTeam = teamMapper.getAllTeam();
        return allTeam;
    }


}
