package com.nau.dao;

import com.nau.entity.Params;
import com.nau.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface UserDao extends Mapper<User> {

  List<User> findBySearch(@Param("params") Params params);

  @Select("select * from user where username = #{username} limit 1")
  User findByUserName(@Param("username") String username);

  @Select("select * from user where username = #{username} and password = #{password} limit 1")
  User findByNameAndPassword(@Param("username") String username, @Param("password") String password);

  /**
   * 根据角色和店铺id查询用户
   *
   * @param role    角色
   * @param salonId 店铺id
   * @return 用户
   */
  @Select("select * from user where role = #{role} and salon_id = #{salon_id}")
  List<User> findByRoleAndSalonId(@Param("role") String role, @Param("salon_id") int salonId);

  /**
   * 根据角色查询用户
   *
   * @param role    角色
   * @return 用户
   */
  @Select("select * from user where role = #{role}  ")
  List<User> findByRole(@Param("role") String role);

}
