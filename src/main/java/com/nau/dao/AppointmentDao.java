package com.nau.dao;

import com.nau.entity.Appointment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentDao extends Mapper<Appointment> {

  /**
   * 查询某条的预约信息
   */
  @Select("SELECT * FROM appointment WHERE id = #{id} limit 1")
  Appointment findById(@Param("id") Integer id);

  /**
   * 查询店铺下的预约信息.
   */
  @Select("SELECT * FROM appointment WHERE salon_id = #{salonId} order by appointment_time desc")
  List<Appointment> findBySalonId(@Param("salonId") Integer salonId);

  /**
   * 查询店铺下的预约信息.
   */
  @Select("SELECT * FROM appointment WHERE salon_id = #{salonId} order by appointment_time desc limit #{count} OFFSET #{skipCount}")
  List<Appointment> findBySalonIdByPage(@Param("salonId") Integer salonId, @Param("count") Integer count, @Param("skipCount") Integer skipCount);

  /**
   * 查询该用户的下的预约信息.
   */
  @Select("SELECT * FROM appointment WHERE salon_id = #{salonId} and technician_id=#{technicianId} order by appointment_time desc")
  List<Appointment> findBySalonIdAndTechnicianId(@Param("salonId") Integer salonId, @Param("technicianId") Integer technicianId);

  /**
   * 查询该用户的下的预约信息.
   */
  @Select("SELECT * FROM appointment WHERE salon_id = #{salonId} and technician_id=#{technicianId} order by appointment_time desc limit #{count}  OFFSET  #{skipCount}")
  List<Appointment> findBySalonIdAndTechnicianIdByPage(@Param("salonId") Integer salonId, @Param("technicianId") Integer technicianId
      , @Param("count") Integer count, @Param("skipCount") Integer skipCount);

  /**
   * 查询顾客的预约信息
   */
  @Select("SELECT * FROM appointment WHERE user_id = #{userId} order by appointment_time desc,id desc;")
  List<Appointment> findByUserId(@Param("userId") Integer userId);

  /**
   * 查询顾客的预约信息
   */
  @Select("SELECT * FROM appointment WHERE user_id = #{userId} order by appointment_time desc,id desc  limit #{count}  OFFSET  #{skipCount}")
  List<Appointment> findByUserIdByPage(@Param("userId") Integer userId, @Param("count") Integer count, @Param("skipCount") Integer skipCount);

  /**
   * 查询technicaion的预约信息.
   */
  @Select("SELECT * FROM appointment WHERE technician_id = #{technicianId} order by appointment_time desc")
  List<Appointment> findByTechnicianId(@Param("technicianId") Integer technicianId);

  /**
   * 查询状态为status的预约信息.
   */
  @Select("SELECT * FROM appointment WHERE status = #{status}")
  List<Appointment> findByStatus(@Param("status") String status);

  /**
   * 查询某段时间仍然有效的预约信息.
   */
  @Select("SELECT * FROM appointment WHERE salon_id = #{salonId} and status in ('PENDING', 'APPROVED','COMPLETED')"
      + " and appointment_time >= #{startTime} and appointment_time <= #{endTime}")
  List<Appointment> findBySalonIdAndAppointmentTime(@Param("salonId") int salonId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}