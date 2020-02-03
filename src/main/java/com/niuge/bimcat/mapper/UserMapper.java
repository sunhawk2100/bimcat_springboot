package com.niuge.bimcat.mapper;

import com.niuge.bimcat.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT id,NAME,PASSWORD FROM user where name = #{value}")
    public User findByName(String name);

    @Select("SELECT id,NAME,PASSWORD,perms FROM user where id = #{value}")
    public User findById(Integer id);
}
