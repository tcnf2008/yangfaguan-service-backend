package com.nau.dao;

import com.nau.entity.Params;
import com.nau.entity.Room;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface RoomDao extends Mapper<Room> {

    List<Room> findBySearch(@Param("params") Params params);
    
    @Update("update room set status = 0 where id = #{ id }")
    void updateById(@Param("id")Integer id);
    
    @Select("select room.*, type.name as typeName from room left join type on room.type_id = type.id")
    List<Room> findAll();
    
    @Select("select Count(*) from room where type_id = #{id}")
    int selectRoomById(@Param("id")Integer id);
}
