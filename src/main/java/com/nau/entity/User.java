package com.nau.entity;

import lombok.Data;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "user")
@Data
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "username")
  private String username;
  @Column(name = "password")
  private String password;
  @Column(name = "name")
  private String name;
  @Column(name = "avatar")
  private String avatar;
  @Column(name = "role")
  private String role;
  @Column(name = "phone")
  private String phone;
  @Column(name = "email")
  private String email;
  /** 店铺id:店长、技师需关联到店铺 */
  @Column(name = "salon_id")
  private Integer salonId;
  @Transient
  private String token;
  @Transient
  private String verCode;
  @Transient
  private String newPassword;
  @Transient
  private String roleName;

  @Override
  public String toString() {
    return "User{" +
        "id=" + id +
        ", username='" + username + '\'' +
        ", password='" + password + '\'' +
        ", name='" + name + '\'' +
        ", avatar='" + avatar + '\'' +
        ", role='" + role + '\'' +
        ", phone='" + phone + '\'' +
        ", email='" + email + '\'' +
        ", token='" + token + '\'' +
        ", verCode='" + verCode + '\'' +
        '}';
  }
}
