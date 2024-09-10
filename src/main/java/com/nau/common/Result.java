package com.nau.common;

public class Result<T> {

  private static final int SUCCESS = 0;

  private int code;
  private String msg;
  private T data;

  public static Result success() {
    Result result = new Result();
    result.setCode(SUCCESS);
    return result;
  }

  public static <T> Result<T> success(T data) {
    Result result = new Result();
    result.setCode(SUCCESS);
    result.setData(data);
    return result;
  }

  public static Result error(String msg) {
    Result result = new Result();
    result.setCode(1);
    result.setMsg(msg);
    return result;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
