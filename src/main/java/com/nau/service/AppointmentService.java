// Copyright 2024 Haipeng.wang . All Rights Reserved.

package com.nau.service;

import cn.hutool.core.collection.CollUtil;
import com.nau.common.JwtTokenUtils;
import com.nau.common.Result;
import com.nau.common.enums.Role;
import com.nau.common.enums.Status;
import com.nau.dao.AppointmentDao;
import com.nau.dao.ServiceDao;
import com.nau.entity.Appointment;
import com.nau.entity.Salon;
import com.nau.entity.User;
import com.nau.service.pojo.TimeSpanValidTechnician;
import com.nau.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

/**
 * 预约服务： 顾客：查看店铺可预约时间+技师、预约，查看预约记录 店长：查看店铺预约记录，确认预约、取消预约、完成预约。
 *
 * @author Haipeng.wang NAU
 * @date 2024-09-05.
 */
@Service
public class AppointmentService {

  private static final Logger log = LoggerFactory.getLogger(AppointmentService.class);

  @Resource
  private AppointmentDao appointmentDao;
  @Resource
  private UserService userService;
  @Resource
  private SalonService salonService;
  @Resource
  private ServiceDao serviceDao;

  /**
   * 顾客查看某日可预约时间与技师
   */
  public List<TimeSpanValidTechnician> findAvailableTime(int salonId, LocalDate date) {
    LocalDate today = LocalDateTime.now().toLocalDate();
    LocalTime nowTime = LocalDateTime.now().toLocalTime().plusMinutes(30);
    LocalDateTime startTime = date.atTime(LocalTime.MIN);
    LocalDateTime endTime = date.atTime(LocalTime.MAX);

    List<Appointment> appointmentList = appointmentDao.findBySalonIdAndAppointmentTime(salonId, startTime, endTime);
    List<User> allTechniciansOfSalon = userService.findByRoleAndSalonId(Role.TECHNICIAN.name(), salonId);

    //从上午8:00到下午21:00，每30分钟作为一个TimeSpanValidTechnician，判断该时间段每个技师是否可用，
    // 如果没有可用的技师，则标记该时间段不可用。否则标记该时间段可用，并把可用客服添加到validTechnicians中。

    List<TimeSpanValidTechnician> timeSpanValidTechnicianList = new ArrayList<>();

    LocalTime currentCheckTime = LocalTime.of(8, 0);

    while (currentCheckTime.isBefore(LocalTime.of(21, 30))) {
      try {
        TimeSpanValidTechnician timeSpanValidTechnician = new TimeSpanValidTechnician();
        timeSpanValidTechnician.setTimeOfDay(DateTimeUtil.convertLocalTimeToString(currentCheckTime));

        List<User> validTechnicians = new ArrayList<>();
        for (User technician : allTechniciansOfSalon) {
          boolean isTechnicianAvailable = true;
          for (Appointment appointment : appointmentList) {
            LocalTime appointmentTime = appointment.getAppointmentTime().toLocalTime();
            LocalTime appointmentEndTime = appointment.getAppointmentTime().plusMinutes(appointment.getTime()).minusSeconds(1).toLocalTime();
            if (appointment.getTechnicianId().equals(technician.getId())
                && (appointmentTime.equals(currentCheckTime)
                || (currentCheckTime.isAfter(appointmentTime) && currentCheckTime.isBefore(appointmentEndTime)))) {
              isTechnicianAvailable = false;
              break;
            }
          }
          if (isTechnicianAvailable) {
            validTechnicians.add(technician);
          }
        }
        timeSpanValidTechnician.setValidTechnicians(validTechnicians);
        if (CollUtil.isNotEmpty(validTechnicians) &&
            (!today.equals(date) || (today.equals(date) && currentCheckTime.isAfter(nowTime)))
        ) {
          timeSpanValidTechnician.setValid(true);
        } else {
          timeSpanValidTechnician.setValid(false);
        }
        timeSpanValidTechnicianList.add(timeSpanValidTechnician);


      } catch (Exception e) {
        log.error("findAvailableTime error", e);
      } finally {
        currentCheckTime = currentCheckTime.plusMinutes(30);
      }
    }
    return timeSpanValidTechnicianList;
  }

  public Result<Appointment> addAppointment(Appointment appointment) {
//    Integer userId = appointment.getUserId();
//    Integer salonId = appointment.getSalonId();
//    Integer technicianId = appointment.getTechnicianId();
    Integer serviceId = appointment.getServiceId();

//    User user = userService.findById(userId);
//    User technician = userService.findById(technicianId);
    com.nau.entity.Service service = serviceDao.selectByPrimaryKey(serviceId);
    Salon salon = salonService.findById(appointment.getSalonId());
//

    appointment.setStatus(Status.PENDING.name());

    appointment.setSalonName(salon.getName());
    appointment.setServiceName(service.getName());
    appointment.setTime(service.getTime());
    appointment.setFinishTime(appointment.getAppointmentTime().plusMinutes(service.getTime()));
    appointment.setCreatedAt(LocalDateTime.now());
    appointment.setUpdatedAt(LocalDateTime.now());

    appointmentDao.insert(appointment);
    return Result.success(appointment);
  }

  public Result<Appointment> setAppointmentStatus(int appointmentId, String status) {
    try {
      Appointment appointment = appointmentDao.selectByPrimaryKey(appointmentId);
      appointment.setStatus(status);
      appointment.setUpdatedAt(LocalDateTime.now());

      appointmentDao.updateByPrimaryKey(appointment);
      return Result.success(appointment);
    } catch (Exception e) {
      log.error("setAppointmentStatus error", e);
      return Result.error("setAppointmentStatus error");
    }
  }

  public Result<List<Appointment>> findByUserId(int userId) {
    try {
      List<Appointment> appointmentList = appointmentDao.findByUserId(userId);
      if (CollUtil.isNotEmpty(appointmentList)) {
        for (Appointment appointment : appointmentList) {
          appointment.setStatusName(Status.valueOf(appointment.getStatus()).getNick());
        }
      }

      return Result.success(appointmentList);
    } catch (Exception e) {
      log.error("findByUserId error", e);
      return Result.error("findByUserId error");
    }
  }

  public Result<List<Appointment>> findByUserId(int userId, Integer pageSize, Integer pageNum) {
    try {
      List<Appointment> appointmentList = appointmentDao.findByUserIdByPage(userId, pageSize, (pageNum - 1) * pageSize);
      if (CollUtil.isNotEmpty(appointmentList)) {
        for (Appointment appointment : appointmentList) {
          appointment.setStatusName(Status.valueOf(appointment.getStatus()).getNick());
        }
      }

      return Result.success(appointmentList);
    } catch (Exception e) {
      log.error("findByUserId error", e);
      return Result.error("findByUserId error");
    }
  }

  public Result<List<Appointment>> findBySalonId(Integer salonId) {
    try {
      User currentUser = JwtTokenUtils.getCurrentUser();

      List<Appointment> appointmentList = null;
      if (Role.TECHNICIAN.name().equalsIgnoreCase(currentUser.getRole()) || Role.MANAGER.name().equalsIgnoreCase(currentUser.getRole())) {
        appointmentList = appointmentDao.findBySalonIdAndTechnicianId(salonId, currentUser.getId());
      } else if (Role.ADMIN.name().equalsIgnoreCase(currentUser.getRole())) {
        appointmentList = appointmentDao.findBySalonId(salonId);
      }

      if (CollUtil.isNotEmpty(appointmentList)) {
        for (Appointment appointment : appointmentList) {
          appointment.setStatusName(Status.valueOf(appointment.getStatus()).getNick());
        }
      }
      return Result.success(appointmentList);
    } catch (Exception e) {
      log.error("findByUserId error", e);
      return Result.error("findByUserId error");
    }
  }

  public Result<List<Appointment>> findBySalonId(Integer salonId, Integer pageSize, Integer pageNum) {
    try {
      User currentUser = JwtTokenUtils.getCurrentUser();

      List<Appointment> appointmentList = null;
      if (Role.TECHNICIAN.name().equalsIgnoreCase(currentUser.getRole()) ) {
        appointmentList = appointmentDao.findBySalonIdAndTechnicianIdByPage(salonId, currentUser.getId(), pageSize, (pageNum - 1) * pageSize);
      }else if (Role.MANAGER.name().equalsIgnoreCase(currentUser.getRole())) {
        appointmentList = appointmentDao.findBySalonIdByPage(currentUser.getSalonId(), pageSize, (pageNum - 1) * pageSize);
      } else if (Role.ADMIN.name().equalsIgnoreCase(currentUser.getRole())) {
        appointmentList = appointmentDao.findBySalonIdByPage(salonId, pageSize, (pageNum - 1) * pageSize);
      }

      if (CollUtil.isNotEmpty(appointmentList)) {
        for (Appointment appointment : appointmentList) {
          appointment.setStatusName(Status.valueOf(appointment.getStatus()).getNick());
        }
      }
      return Result.success(appointmentList);
    } catch (Exception e) {
      log.error("findByUserId error", e);
      return Result.error("findByUserId error");
    }
  }
}
