package com.nau.controller;

import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.PageInfo;
import com.nau.common.LogAround;
import com.nau.common.Result;
import com.nau.common.enums.Role;
import com.nau.entity.Params;
import com.nau.entity.User;
import com.nau.service.UserService;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

  private static final Logger log = LoggerFactory.getLogger(UserController.class);

  @Autowired
  private UserService userService;

//  @GetMapping("/findAll")
//  public Result findAll() {
//    List<User> list = userService.findAll();
//    return Result.success(list);
//  }

  //查询店铺下店长或者技师
  @GetMapping("/findAllBySalonId/{salonId}")
  public Result findAllBySalonId(@PathVariable Integer salonId) {
    try {
      List<User> managerList = userService.findByRoleAndSalonId(Role.MANAGER.name(), salonId);
      List<User> technicianList = userService.findByRoleAndSalonId(Role.TECHNICIAN.name(), salonId);
      if (managerList == null) {
        managerList = new ArrayList<>();
      }
      if (CollUtil.isNotEmpty(technicianList)) {
        managerList.addAll(technicianList);
      }
      if (CollUtil.isNotEmpty(managerList)) {
        for (User userDb : managerList) {
          if (userDb != null && StringUtils.isNotBlank(userDb.getRole())) {
            Role role = Role.valueOf(userDb.getRole());
            userDb.setRoleName(role.getNick());
          }
        }
      }
      return Result.success(managerList);
    } catch (Exception e) {
      log.error("查询店铺下店长或者技师失败", e);
      return Result.error("查询店铺下店长或者技师失败 " + e.toString());
    }
  }

  //查询店铺下店长或者技师
  @GetMapping("/findByRoleAndSalonId/{role}/{salonId}")
  public Result findAllBySalonId(@PathVariable String role, @PathVariable Integer salonId) {
    try {
      List<User> userList = userService.findByRoleAndSalonId(role, salonId);
      if (userList == null) {
        userList = new ArrayList<>();
      }
      return Result.success(userList);
    } catch (Exception e) {
      log.error("查询店铺下用户失败", e);
      return Result.error("查询店铺下用户失败 " + e.toString());
    }
  }

  //查询店铺下店长或者技师
  @GetMapping("/findAllUser")
  public Result findAllBySalonId() {
    try {
      List<User> userList = userService.findByRole(Role.USER.name());
      if (userList == null) {
        userList = new ArrayList<>();
      }
      return Result.success(userList);
    } catch (Exception e) {
      log.error("查询用户失败", e);
      return Result.error("查询用户失败 " + e.toString());
    }
  }

  @PostMapping("/saveOrUpdate")
  @LogAround("增加或修改用户信息")
  public Result saveOrUpdate(@RequestBody User user) {
    try {
      if (user.getId() == null) {
        userService.add(user);
      } else {
        userService.update(user);
      }
      return Result.success();
    } catch (Exception e) {
      log.error("exception: {}", e, e);
      return Result.error(e.toString());
    }
  }


  @GetMapping("/search")
  public Result findBySearch(Params params) {
    log.info("拦截器已放行，正式调用接口内部，查询管理员信息");
    PageInfo<User> info = userService.findBySearch(params);
    return Result.success(info);
  }

  @DeleteMapping("/{id}")
  @LogAround("删除了一个用户")
  public Result delete(@PathVariable Integer id) {
    userService.delete(id);
    return Result.success();
  }
}
