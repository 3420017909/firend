package org.example.friend.AOP;


import cn.hutool.bloomfilter.BloomFilter;
import jakarta.annotation.Resource;
import org.apache.tomcat.Jar;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.friend.exception.CustomException;
import org.springframework.stereotype.Component;

import static org.example.friend.constant.BloomFilterConstant.TEAM_BLOOM_PREFIX;
import static org.example.friend.constant.BloomFilterConstant.USER_BLOOM_PREFIX;

@Aspect
@Component
public class BloomFilterAOP {
    @Resource
    BloomFilter bloomFilter;

    @After("execution(* org.example.friend.service.imp.UserServiceimp.userRegister(..)))")
    public void addUser(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        bloomFilter.add(USER_BLOOM_PREFIX + args[1]);
    }

    @After("execution(* org.example.friend.service.imp.TeamServiceimp.addTeam(..))")
    public void addTeam(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        bloomFilter.add(TEAM_BLOOM_PREFIX + args[1]);
    }

    @Before("execution(* org.example.friend.service.imp.UserServiceimp.selectUserByName(..)))")
    public void findUser(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        boolean contains = bloomFilter.contains(USER_BLOOM_PREFIX + args[0]);
        if (!contains) {
            throw new CustomException(4006, "bloom无结果");
        }
    }

    @Before("execution(* org.example.friend.service.imp.TeamServiceimp.findTeam(..)))")
    public void findTeam(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        boolean contains = bloomFilter.contains(TEAM_BLOOM_PREFIX + args[0]);
        if (!contains) {
            throw new CustomException(4006, "bloom无结果");
        }
    }
}
