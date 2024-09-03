package org.example.friend.controller;



import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.example.friend.request.TeamAddRequest;
import org.example.friend.request.TeamRequest;
import org.example.friend.POjO.entity.Team;
import org.example.friend.response.Response;
import org.example.friend.response.ResponseEntity;
import org.example.friend.service.TeamService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/team")
public class TeamController {
    @Resource
    private TeamService teamService;


    @PostMapping("/add")
    public Response addTeam(@RequestBody TeamAddRequest addTeam, HttpServletRequest request) {

        teamService.addTeam(addTeam, request);
        return ResponseEntity.success("创建成功", null);
    }

    @PutMapping("/update")
    public Response updateTeam(@RequestBody Team team, HttpServletRequest request) {
        teamService.updateTeam(team, request);
        return ResponseEntity.success("更新成功", null);
    }
//TODO 因为还不知道怎么处理时间，所以过期时间还没设置，如果需要测试记得删除if的过期时间的判断
    @GetMapping("/find")
    public Response findTeam(@RequestParam("name") String name, HttpServletRequest request) {
        Team team = teamService.findTeam(name, request);
        return ResponseEntity.success("搜索成功", team);
    }

    @GetMapping("/finds")
    public Response findUserTeam(@RequestParam("teamid") String teamid,
                                 @RequestParam(defaultValue = "1") int pageNum,
                                 @RequestParam(defaultValue = "10") int pageSize
                                 ) {
        List<String> userList = teamService.findUserTeam(teamid, pageNum,pageSize );
        return ResponseEntity.success("团队名单展示", userList);
    }
//TODO 这个也有过期时间的判断
    @PostMapping("/join")
    public Response joinTeam(@RequestBody TeamRequest teamRequest, HttpServletRequest request) {
        teamService.joinTeam(teamRequest, request);
        return ResponseEntity.success("加入成功", null);
    }

    @DeleteMapping("/quit")
    public Response quitTeam(@RequestParam("teamid") long teamId, HttpServletRequest request) {
        teamService.quitTeam(teamId, request);
        return ResponseEntity.success("退出成功", null);
    }

    @DeleteMapping("/delete")
    public Response deleteTeam(@RequestParam("teamid") long teamId, HttpServletRequest request) {
        teamService.deleteTeam(teamId, request);
        return ResponseEntity.success("删除成功", null);
    }

    @DeleteMapping("/kick")
    public Response kickTeam(@RequestParam("teamid") long teamId, @RequestParam("userid") long userId, HttpServletRequest request) {
        teamService.kickUser(teamId, userId, request);
        return ResponseEntity.success("踢出成功", null);
    }

    @GetMapping("/count")
    public Response userCount(@RequestParam("teamid") long teamId) {
        int userCount = teamService.userCount(teamId);
        return ResponseEntity.success("队伍人数",userCount);
    }
}
