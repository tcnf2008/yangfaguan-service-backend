package com.nau.entity;

import javax.persistence.*;

@Table(name = "reserve")
public class Reserve {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "name")
    private String name;
    @Column(name = "student_name")
    private String studentName;
    @Column(name = "teacher_name")
    private String teacherName;
    @Column(name = "time")
    private String time;
    @Column(name = "use_time")
    private String useTime;
    @Column(name = "reserve_status")
    private Integer ReserveStatus;
    @Column(name = "use_status")
    private Integer useStatus;
    @Column(name = "room_id")
    private Integer roomId;
    
    public Integer getRoomId() {
        return roomId;
    }
    
    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getStudentName() {
        return studentName;
    }
    
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    
    public String getTeacherName() {
        return teacherName;
    }
    
    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
    
    public String getTime() {
        return time;
    }
    
    public void setTime(String time) {
        this.time = time;
    }
    
    public String getUseTime() {
        return useTime;
    }
    
    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }
    
    public Integer getReserveStatus() {
        return ReserveStatus;
    }
    
    public void setReserveStatus(Integer reserveStatus) {
        ReserveStatus = reserveStatus;
    }
    
    public Integer getUseStatus() {
        return useStatus;
    }
    
    public void setUseStatus(Integer useStatus) {
        this.useStatus = useStatus;
    }
}
