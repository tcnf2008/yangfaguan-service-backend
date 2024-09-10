package com.nau.service;

import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nau.common.CaptureConfig;
import com.nau.common.JwtInterceptor;
import com.nau.common.JwtTokenUtils;
import com.nau.common.Result;
import com.nau.common.enums.Role;
import com.nau.dao.UserDao;
import com.nau.entity.User;
import com.nau.entity.Params;
import com.nau.exception.CustomException;
import com.wf.captcha.utils.CaptchaUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Service;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务类。
 */
@Service
public class UserService {

  private static final Logger log = LoggerFactory.getLogger(UserService.class);

  @Resource
  private UserDao userDao;

  public List<User> findAll() {
    return userDao.selectAll();
  }

  public PageInfo<User> findBySearch(Params params) {
    // 分页查询
    PageHelper.startPage(params.getPageNum(), params.getPageSize());
    List<User> list = userDao.findBySearch(params);
    return PageInfo.of(list);
  }

  public void add(User user) {
    // 1. 用户名一定要有，否则不让新增（后面需要用户名登录）
    if (user.getUsername() == null || "".equals(user.getUsername())) {
      throw new CustomException("用户名不能为空");
    }
    // 2. 进行重复性判断，同一名字不允许重复新增
    User userDb = userDao.findByUserName(user.getUsername());
    if (userDb != null) {
      throw new CustomException("该用户名已存在");
    }
    // 初始化一个密码
    if (user.getPassword() == null) {
      user.setPassword("123456");
    }
    userDao.insertSelective(user);
  }

  //注册
  public Result<User> registerUser(User userData) {
    if (StringUtils.isBlank(userData.getUsername())) {
      return Result.error("用户名不能为空");
    }
    if (StringUtils.isBlank(userData.getPassword())) {
      return Result.error("密码不能为空");
    }
    if (StringUtils.isBlank(userData.getRole())) {
      return Result.error("角色不能为空");
    }
    if (StringUtils.isBlank(userData.getPhone())) {
      return Result.error("手机号不能为空");
    }
    if (StringUtils.isBlank(userData.getName())) {
      return Result.error("昵称不能为空");
    }

    User dbUser = userDao.findByUserName(userData.getUsername());
    if (dbUser != null) {
      return Result.error("该用户名已存在");
    }

    User user = null;
    try {
      user = new User();
      BeanUtils.copyProperties(userData, user);
//    user.setRole(Role.USER.name());

      add(user);
    } catch (Exception e) {
      log.error("注册失败", e);
      return Result.error("注册失败：" + e.toString());
    }

    return Result.success(user);
  }

  public void update(User admin) {
    userDao.updateByPrimaryKeySelective(admin);
  }

  public void delete(Integer id) {
    userDao.deleteByPrimaryKey(id);
  }

  public User login(User user, String key, HttpServletRequest request) {
    // 判断验证码对不对
    if (!user.getVerCode().toLowerCase().equals(CaptureConfig.CAPTURE_MAP.get(key))) {
      // 如果不相等，说明验证不通过
      CaptchaUtil.clear(request);
      throw new CustomException("验证码不正确");
    }
    CaptureConfig.CAPTURE_MAP.remove(key);

    // 1. 进行一些非空判断
    if (user.getUsername() == null || "".equals(user.getUsername())) {
      throw new CustomException("用户名不能为空");
    }
    if (user.getPassword() == null || "".equals(user.getPassword())) {
      throw new CustomException("密码不能为空");
    }
    // 2. 从数据库里面根据这个用户名和密码去查询对应的管理员信息，
    User userDb = userDao.findByNameAndPassword(user.getUsername(), user.getPassword());
    if (userDb == null) {
      // 如果查出来没有，那说明输入的用户名或者密码有误，提示用户，不允许登录
      throw new CustomException("用户名或密码输入错误");
    }
    // 如果查出来了有，那说明确实有这个管理员，而且输入的用户名和密码都对；
    // 生成该登录用户对应的token，然后跟着user一起返回到前台
    String tokenData = userDb.getId() + "-" + userDb.getRole();
    String token = JwtTokenUtils.genToken(tokenData, userDb.getPassword());
    userDb.setToken(token);
    return userDb;
  }

  public User findById(Integer id) {
    return userDao.selectByPrimaryKey(id);
  }

  /**
   * 根据店铺查询店长或者技师.
   */
  public List<User> findByRoleAndSalonId(String role, int salonId) {
    return userDao.findByRoleAndSalonId(role, salonId);
  }

  /**
   * 根据店铺查询店长或者技师.
   */
  public List<User> findByRole(String role) {
    return userDao.findByRole(role);
  }


  /**
   * 用户修改密码
   */
  public void updatePassword(User account) {
    User dbUser = userDao.findByUserName(account.getUsername());
    if (ObjectUtil.isNull(dbUser)) {
      throw new CustomException("用户不存在");
    }
    if (!account.getPassword().equals(dbUser.getPassword())) {
      throw new CustomException("原密码不正确");
    }
    dbUser.setPassword(account.getNewPassword());
    userDao.updateByPrimaryKey(dbUser);
  }

  /**
   * 修改个人信息
   */
  public void updatePersonal(User account) {
    User admin = new User();
    BeanUtils.copyProperties(account, admin);
    update(admin);
  }
}
