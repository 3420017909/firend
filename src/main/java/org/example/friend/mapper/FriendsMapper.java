package org.example.friend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.friend.POjO.entity.Friends;

import java.util.List;

/**
 * @author 0.0
 * @description 针对表【friends(好友申请管理表)】的数据库操作Mapper
 * @createDate 2024-06-13 15:08:32
 * @Entity org.example.friend.POjO.entity.Friends
 */
@Mapper
public interface FriendsMapper extends BaseMapper<Friends> {

    void addFriends(Friends friends);

    @Select("select * from friends where from_id=#{id}")
    List<Friends> fromRecords(@Param("page")Page<Friends> page, @Param("id") long id);

    @Select("select * from friends where receive_id=#{id}")
    List<Friends> receiveRecords(@Param("page")Page<Friends> page, @Param("id") long id);

    @Select("select * from friends where id=#{id}")
    Friends receiveRecordsById(long id);

    @Update("update friends set status=#{status} where id=#{id}")
    void updateRecord(Integer status, long id);

    @Update("update friends set status=#{status} where id=#{id}")
    void updatefrom(Integer status, Long id);


}




