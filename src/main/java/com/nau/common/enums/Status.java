package com.nau.common.enums;

public enum Status {

  PENDING("待确认"), APPROVED("预约成功"), REJECTED("预约失败")
  , CANCELED("已取消预约"), COMPLETED("服务完毕");
  String nick;

  Status(String nick) {
    this.nick = nick;
  }

  public String getNick(){
    return nick;
  }
}