package com.nau.common.enums;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

public enum Role {

  ADMIN("管理员"),
  MANAGER("店长"),
  TECHNICIAN("技师"),
  USER("顾客");

  /**
   * 外显名称.
   */
  String nick;

  Role(String nick) {
    this.nick = nick;
  }

  public String getNick() {
    return nick;
  }
}