package com.nau.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "appointment")
public class Appointment implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "user_id")
  private Integer userId;

  @Column(name = "user_name")
  private String userName;

  @Column(name = "technician_id")
  private Integer technicianId;

  @Column(name = "technician_name")
  private String technicianName;

  @Column(name = "service_id")
  private Integer serviceId;

  @Column(name = "service_name")
  private String serviceName;

  @Column(name = "salon_id")
  private Integer salonId;

  @Column(name = "salon_name")
  private String salonName;

  @Column(name = "appointment_time")
  private LocalDateTime appointmentTime;

  @Column(name = "time")
  private Integer time;

  @Column(name = "finish_time")
  private LocalDateTime finishTime;

  @Column(name = "status")
  private String status;

  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Transient
  private String statusName;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }
}