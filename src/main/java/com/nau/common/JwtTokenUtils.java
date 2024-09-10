package com.nau.common;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.nau.entity.User;
import com.nau.entity.User;
import com.nau.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtTokenUtils {

  private static UserService staticUserService;
  //  private static AdminService staticAdminService;
//  private static TeacherService staticTeacherService;
//  private static StudentService staticStudentService;
  private static final Logger log = LoggerFactory.getLogger(JwtTokenUtils.class);

  @Resource
  private UserService userService;
//  @Resource
//  private AdminService adminService;
//  @Resource
//  private TeacherService teacherService;
//  @Resource
//  private StudentService studentService;

  @PostConstruct
  public void setUserService() {
    staticUserService = userService;
//    staticAdminService = adminService;
//    staticTeacherService = teacherService;
//    staticStudentService = studentService;
  }

  /**
   * 生成token
   */
  public static String genToken(String userId, String password) {
    return JWT.create().withAudience(userId) // 将 userId 保存到 token 里面,作为载荷
        .withExpiresAt(DateUtil.offsetHour(new Date(), 2400)) // 6小时后token过期
        .sign(Algorithm.HMAC256(password)); // 以 password 作为 token 的密钥
  }

  /**
   * 获取当前登录的用户信息
   */
  public static User getCurrentUser() {
    String token = null;
    try {
      HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
      token = request.getHeader("token");
      if (StrUtil.isBlank(token)) {
        token = request.getParameter("token");
      }
      if (StrUtil.isBlank(token)) {
        log.error("获取token失败， token: {}", token);
        return null;
      }
      // 解析token，获取用户的id
      String tokenData = JWT.decode(token).getAudience().get(0);
      String userId = tokenData.split("-")[0];
      String role = tokenData.split("-")[1];

      User currentUser = staticUserService.findById(Integer.valueOf(userId));
      return currentUser;
    } catch (Exception e) {
      log.error("获取当前登录的用户信息失败, token={}", token, e);
    }
    return new User();//返回空的账号对象
  }
}