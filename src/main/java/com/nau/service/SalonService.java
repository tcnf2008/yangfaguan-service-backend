// SalonService.java

package com.nau.service;

import com.nau.common.JwtTokenUtils;
import com.nau.common.enums.Role;
import com.nau.dao.AppointmentDao;
import com.nau.dao.SalonDao;
import com.nau.dao.UserDao;
import com.nau.entity.Appointment;
import com.nau.entity.Salon;
import com.nau.entity.User;
import com.nau.exception.CustomException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
public class SalonService {

  @Resource
  private SalonDao salonDao;
  @Resource
  private UserDao userDao;
  @Resource
  private AppointmentDao appointmentDao;

  public List<Salon> findAll() {
    User currentUser = JwtTokenUtils.getCurrentUser();

    if (Role.TECHNICIAN.name().equalsIgnoreCase(currentUser.getRole()) || Role.MANAGER.name().equalsIgnoreCase(currentUser.getRole())) {
      return Arrays.asList(salonDao.selectByPrimaryKey(currentUser.getSalonId()));
    } else if (Role.ADMIN.name().equalsIgnoreCase(currentUser.getRole())) {
      return salonDao.findAll();
    }
    return salonDao.findAll();
  }

  public Salon findById(Integer id) {
    return salonDao.selectByPrimaryKey(id);
  }

  public void add(Salon salon) {
    if (salon.getName() == null || salon.getName().isEmpty()) {
      throw new CustomException("店铺名称不能为空");
    }
    salonDao.insertSelective(salon);
  }

  public void update(Salon salon) {
    if (salon.getName() == null || salon.getName().isEmpty()) {
      throw new CustomException("店铺名称不能为空");
    }
    salonDao.updateByPrimaryKeySelective(salon);
  }

  public void delete(Integer id) {
    salonDao.deleteByPrimaryKey(id);
  }


}