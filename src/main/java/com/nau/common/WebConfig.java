package com.nau.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Resource
  private JwtInterceptor jwtInterceptor;

  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {
    // 指定controller统一的接口前缀，相当于：在url上拼了一个 /api/xxx
    configurer.addPathPrefix("/api", clazz -> clazz.isAnnotationPresent(RestController.class));
  }

  // 加自定义拦截器JwtInterceptor，设置拦截规则
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(jwtInterceptor).addPathPatterns("/api/**")
        .excludePathPatterns("/api/file/**")
        .excludePathPatterns("/api/files/**")
        .excludePathPatterns("/api/validCode")
        .excludePathPatterns("/api/type/upload")
        .excludePathPatterns("/api/login")
        .excludePathPatterns("/api/register")
        .excludePathPatterns("/api/getUser")
        .excludePathPatterns("/api/out")
        .excludePathPatterns("/api/updatePassword")
        .excludePathPatterns("/api/updatePersonal");
  }
}