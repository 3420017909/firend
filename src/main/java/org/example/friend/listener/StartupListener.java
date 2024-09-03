package org.example.friend.listener;

import cn.hutool.bloomfilter.BitSetBloomFilter;
import cn.hutool.bloomfilter.BloomFilter;
import cn.hutool.bloomfilter.BloomFilterUtil;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.example.friend.POjO.entity.Team;
import org.example.friend.POjO.entity.User;
import org.example.friend.service.TeamService;
import org.example.friend.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static org.example.friend.constant.BloomFilterConstant.*;

@Configuration
@Log4j2
public class StartupListener implements CommandLineRunner {
    @Resource
    UserService userService;
    @Resource
    TeamService teamService;

    @Override
    public void run(String... args) throws Exception {
        bloomFilterInit();
        log.info("布隆过滤器已加载数据");

    }

    @Bean
    public BloomFilter bloomFilterInit() {
        BitSetBloomFilter bloomFilter = BloomFilterUtil.createBitSet(
                PRE_OPENED_MAXIMUM_INCLUSION_RECORD,
                EXPECTED_INCLUSION_RECORD,
                HASH_FUNCTION_NUMBER
        );
        List<User> allUser = userService.getAllUser();
        for (User user : allUser) {
            bloomFilter.add(USER_BLOOM_PREFIX + user.getUsername());
        }
        List<Team> allTeam = teamService.getAllTeam();
        for (Team team : allTeam) {
            bloomFilter.add(TEAM_BLOOM_PREFIX + team.getName());
        }
        return bloomFilter;
    }
}
