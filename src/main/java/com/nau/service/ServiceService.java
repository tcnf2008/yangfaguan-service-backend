// ServiceService.java
package com.nau.service;

import com.nau.dao.ServiceDao;
import com.nau.entity.Service;
import com.nau.exception.CustomException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ServiceService {

  @Resource
  private ServiceDao serviceDao;

  public List<Service> findAll() {
    return serviceDao.findAll();
  }

  public Service findById(Integer id) {
    return serviceDao.selectByPrimaryKey(id);
  }

  public void add(Service service) {
    if (service.getName() == null || service.getName().isEmpty()) {
      throw new CustomException("服务名称不能为空");
    }
    serviceDao.insertSelective(service);
  }

  public void update(Service service) {
    if (service.getName() == null || service.getName().isEmpty()) {
      throw new CustomException("服务名称不能为空");
    }
    serviceDao.updateByPrimaryKeySelective(service);
  }

  public void delete(Integer id) {
    serviceDao.deleteByPrimaryKey(id);
  }
}