package org.example.friend.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;
import org.example.friend.POjO.entity.User;
import org.example.friend.request.ExcelRequest;

import java.util.List;

/**
* @author 0.0
* @description 针对表【user】的数据库操作Mapper
* @createDate 2024-06-08 16:43:37
* @Entity org.example.friend.User
*/
@Mapper
public interface UserMapper {
    @Select("select count(*) from user where id=#{id}")
    Integer userCount(@Param("id") long id);

    @Select("select id,phone,username,password,profile,gender,status,role,tage from user where id=#{id}")
    User selectUserById(long id);
@Select("select * from user where username=#{username}")
    User getUserByName(String username);
    @Update("update user set password=#{password} where username=#{username} ")
    void updatePwd(String password, String username);


    void updateUser(User user);

    @Select("SELECT id,phone,username,profile,gender,status,role,tage FROM user " +
            "WHERE username LIKE CONCAT('%', #{username}, '%')")
    List<User> selectUserByName(@Param("page")Page<User> page, @Param("username")String userName);

    @Insert("insert into user(id, phone,username, password, profile, gender, status, role, tage) " +
            "values(#{user.id},#{user.phone},#{user.username},#{user.password},#{user.profile},#{user.gender},#{user.status},#{user.role},#{user.tage})")
    void insertUser(@Param("user") User user);

    @Update("update user set tage=#{newTage} where id=#{id}")
    void updateTage(String newTage, long id);


    List<User> selectUserByTage(@Param("page")Page<User> page,@Param("tage") List<String> tage);

    @Update("update user set status=1 where id=#{id}")
    void updateStatus(long id);
@Select("select count(*) from user where username=#{username}")
    Integer userCountByName(String username);
@Select("select * from user")
    List<User> getAllUser();
@Select("select * from user")
    List<ExcelRequest> exportUser();
}




