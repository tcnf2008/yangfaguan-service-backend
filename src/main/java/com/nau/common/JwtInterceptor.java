package com.nau.common;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.nau.entity.User;
import com.nau.exception.CustomException;
import com.nau.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt拦截器
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

  private static final Logger log = LoggerFactory.getLogger(JwtInterceptor.class);
  @Resource
  private UserService userService;

  //    @Resource
//    private AdminService adminService;
//    @Autowired
//    private TeacherService teacherService;
//
//    @Autowired
//    private StudentService studentService;
//
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    // 1. 从http请求的header中获取token
    String token = request.getHeader("token");
    if (StrUtil.isBlank(token)) {
      // 如果没拿到，我再去参数里面拿一波试试  /api/admin?token=xxxxx
      token = request.getParameter("token");
    }
    // 2. 开始执行认证
    if (StrUtil.isBlank(token)) {
      throw new CustomException("无token，请重新登录");
    }
    // 获取 token 中的userId
    User account = null;
    try {
      String tokenData = JWT.decode(token).getAudience().get(0);
      String userId = tokenData.split("-")[0];
      String role = tokenData.split("-")[1];
      // 根据token中的userid查询数据库

      account = userService.findById(Integer.parseInt(userId));

    } catch (Exception e) {
      String errMsg = "token验证失败，请重新登录";
      log.error(errMsg + ", token=" + token, e);
      throw new CustomException(errMsg);
    }
    if (account == null) {
      throw new CustomException("用户不存在，请重新登录");
    }
    try {
      // 用户密码加签验证 token
      JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(account.getPassword())).build();
      jwtVerifier.verify(token); // 验证token
    } catch (JWTVerificationException e) {
      throw new CustomException("token验证失败，请重新登录");
    }
    log.debug("验证成功");
    return true;
  }
}