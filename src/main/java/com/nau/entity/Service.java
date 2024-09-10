// Copyright 2024 Haipeng.wang . All Rights Reserved.

package com.nau.entity;

import lombok.Data;
import org.springframework.cglib.core.Local;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Haipeng.wang NAU
 * @date 2024-09-04.
 */
@Entity
@Data
@Table(name = "service")
public class Service implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "category")
  private String category;

  @Column(name = "remark")
  private String remark;

  @Column(name = "time")
  private Integer time;

  @Column(name = "description")
  private String description;

  @Column(name = "pic")
  private String pic;

  @Column(name = "pic_introduce")
  private String picIntroduce;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  // getters and setters
}
