package com.nau.controller;

import cn.hutool.core.util.ObjectUtil;
import com.nau.common.LogAround;
import com.nau.common.JwtTokenUtils;
import com.nau.common.Result;
import com.nau.common.enums.Role;
import com.nau.entity.User;
import com.nau.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class WebController {

  private static final Logger log = LoggerFactory.getLogger(WebController.class);

  @Autowired
  private UserService userService;


  /*
   * 登录
   */
  @PostMapping("/login")
  @LogAround("登录该系统")
  public Result login(@RequestBody User account, @RequestParam String key, HttpServletRequest request) {
    if (ObjectUtil.isEmpty(account.getUsername()) || ObjectUtil.isEmpty(account.getPassword())) {
      return Result.error("账号或者密码为空");
    }
    User userDb = userService.login(account, key, request);
    try {
      if (userDb != null && StringUtils.isNotBlank(userDb.getRole())) {
        Role role = Role.valueOf(userDb.getRole());
        userDb.setRoleName(role.getNick());
      }
    } catch (IllegalArgumentException e) {
      log.error("exception ", e);
    }
    return Result.success(userDb);
  }

  /**
   * 顾客注册
   */
  @PostMapping("/register")
  public Result register(@RequestBody User account) {
    if (ObjectUtil.isEmpty(account.getUsername()) || ObjectUtil.isEmpty(account.getPassword())
        || ObjectUtil.isEmpty(account.getRole())) {
      return Result.error("账号或者密码为空");
    }

    return userService.registerUser(account);
//    return Result.success();
  }

  @GetMapping("/getUser")
  public Result getCurrentUser() {
    User userDb = JwtTokenUtils.getCurrentUser();
    try {
      if (userDb != null && StringUtils.isNotBlank(userDb.getRole())) {
        Role role = Role.valueOf(userDb.getRole());
        userDb.setRoleName(role.getNick());
      }
    } catch (IllegalArgumentException e) {
      log.error("exception ", e);
    }
    return Result.success(userDb);
  }

  @PostMapping("/updatePassword")
  @LogAround("修改个人密码，退出系统")
  public Result updatePassword(@RequestBody User account) {
    if (ObjectUtil.isEmpty(account.getUsername()) || ObjectUtil.isEmpty(account.getPassword())
        || ObjectUtil.isEmpty(account.getRole())) {
      return Result.error("账号或者密码为空");
    }

    userService.updatePassword(account);
    return Result.success();
  }

  @PostMapping("/updatePersonal")
  @LogAround("修改个人信息")
  public Result updatePersonal(@RequestBody User account) {
    if (ObjectUtil.isEmpty(account.getName())) {
      return Result.error("姓名不能为空");
    }

    userService.updatePersonal(account);

//    if ("TEACHER".equals(account.getRole())) {
//      teacherService.updatePersonal(account);
//    }
//    if ("STUDENT".equals(account.getRole())) {
//      studentService.updatePersonal(account);
//    }
    return Result.success();
  }

  @PostMapping("/out")
  @LogAround("退出该系统")
  public Result out(@RequestBody User account) {
    return Result.success(account);
  }

}
