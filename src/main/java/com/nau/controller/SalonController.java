// Copyright 2024 Haipeng.wang . All Rights Reserved.

package com.nau.controller;

import com.nau.common.Result;
import com.nau.entity.Appointment;
import com.nau.entity.Salon;
import com.nau.service.AppointmentService;
import com.nau.service.SalonService;
import com.nau.service.UserService;
import com.nau.service.pojo.TimeSpanValidTechnician;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Haipeng.wang NAU
 * @date 2024-09-04.
 */
@CrossOrigin
@RestController
@RequestMapping("/salon")
public class SalonController {

  private static final Logger log = LoggerFactory.getLogger(SalonController.class);

  @Autowired
  private SalonService salonService;
  @Autowired
  private UserService userService;
  @Autowired
  private AppointmentService appointmentService;

  @GetMapping("/findAll")
  public Result<List<Salon>> findAll() {
    List<Salon> list = salonService.findAll();
    return Result.success(list);
  }

  //增加或者修改店铺信息
  @PostMapping("/saveOrUpdateSalon")
  public Result<Object> save(@RequestBody Salon salon) {
    try {
      if (salon.getId() == null) {
        salonService.add(salon);
      } else {
        salonService.update(salon);
      }
      return Result.success();
    } catch (Exception e) {
      log.error("exception: {}", e, e);
      return Result.error(e.toString());
    }
  }

  //删除店铺信息
  @DeleteMapping("/{id}")
  public Result delete(@PathVariable Integer id) {
    try {
      salonService.delete(id);
      return Result.success();
    } catch (Exception e) {
      log.error("exception: {}", e, e);
      return Result.error(e.toString());
    }
  }

  //查看店铺下可预约时间段
  @GetMapping("/findAvailableTime")
  public Result<List<TimeSpanValidTechnician>> findAvailableTime(@RequestParam Integer salonId, @RequestParam String date) {
    return Result.success(appointmentService.findAvailableTime(salonId, LocalDate.parse(date)));
  }

  @PostMapping("/addAppointment")
  public Result<Appointment> addAppointment(@RequestBody Appointment appointment) {
    return appointmentService.addAppointment(appointment);
  }

  @PostMapping("/setAppointmentStatus")
  public Result<Appointment> setAppointmentStatus(@RequestBody Appointment appointment) {
//    Result<Appointment> setAppointmentStatus(int appointmentId, String status);
    return appointmentService.setAppointmentStatus(appointment.getId(), appointment.getStatus());
  }

  @GetMapping("/findByUserId")
  public Result<List<Appointment>> findByUserId(@RequestParam Integer userId, @RequestParam Integer pageSize, @RequestParam Integer pageNum) {
    return appointmentService.findByUserId(userId, pageSize, pageNum);
  }

  @GetMapping("/findBySalonId")
  public Result<List<Appointment>> findBySalonId(@RequestParam Integer salonId, @RequestParam Integer pageSize, @RequestParam Integer pageNum) {
    return appointmentService.findBySalonId(salonId, pageSize, pageNum);
  }
}
