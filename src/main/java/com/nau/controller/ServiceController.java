// ServiceController.java

package com.nau.controller;

import com.nau.common.Result;
import com.nau.entity.Service;
import com.nau.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service")
public class ServiceController {

  @Autowired
  private ServiceService serviceService;

  @GetMapping("/findAll")
  public Result<List<Service>> findAll() {
    List<Service> list = serviceService.findAll();
    return Result.success(list);
  }

  @GetMapping("/findById/{id}")
  public Result<Service> findById(@PathVariable Integer id) {
    Service service = serviceService.findById(id);
    return Result.success(service);
  }

  @PostMapping("/saveOrUpdate")
  public Result<Object> saveOrUpdate(@RequestBody Service service) {
    if (service.getId() == null) {
      serviceService.add(service);
    } else {
      serviceService.update(service);
    }
    return Result.success();
  }

  @DeleteMapping("/delete/{id}")
  public Result<Object> delete(@PathVariable Integer id) {
    serviceService.delete(id);
    return Result.success();
  }
}