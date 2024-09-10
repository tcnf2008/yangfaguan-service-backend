// Copyright 2024 Haipeng.wang . All Rights Reserved.

package com.nau.util;


import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Haipeng.wang NAU
 * @date 2024-09-05.
 */
public final class DateTimeUtil {

  private static final ZoneOffset ZONE_OFFSET = ZoneOffset.ofHours(8);

  private DateTimeUtil() {

  }

  public static long convertToMS(LocalDateTime localDateTime) {
    ZoneId zoneId1 = ZoneId.of("+8");
    ZonedDateTime zdt = ZonedDateTime.of(localDateTime, zoneId1);
    return zdt.toInstant().toEpochMilli();
  }


  /**
   * 根据时间戳创建LocalDateTime 10位时间戳 秒级
   *
   * @param second 10位时间戳 秒级
   * @return LocalDateTime
   */
  public static LocalDateTime msToLocalDateTime(Long second) {
    if (second == null) {
      return null;
    }
    if (second == 0) {
      return LocalDateTime.MIN;
    }
    if (second.toString().length() == 10) {
      return LocalDateTime.ofEpochSecond(second, 0, ZONE_OFFSET);
    } else if (second.toString().length() == 13) {
      return Instant.ofEpochMilli(second).atZone(ZONE_OFFSET).toLocalDateTime();
    }
    return null;
  }

  /**
   * localDateTime转时间戳 13位时间戳 毫秒级
   *
   * @param localDateTime localDateTime
   * @return 13位时间戳 毫秒级
   */
  public static Long localDateTimeToMs(LocalDateTime localDateTime) {
    return localDateTime.toInstant(ZONE_OFFSET).toEpochMilli();
  }

  /**
   * date转LocalDateTime
   *
   * @param date date
   * @return LocalDateTime
   */
  public static LocalDateTime dateToLocalDateTime(Date date) {
    return date.toInstant().atOffset(ZONE_OFFSET).toLocalDateTime();
  }


  /**
   * localDateTime转字符串
   *
   * @param localDateTime localDateTime
   * @return localDateTime字符串
   */
  public static String localDateTimeToString(LocalDateTime localDateTime) {
    // 指定模式
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    // 将 LocalDateTime 格式化为字符串
    return localDateTime.format(dateTimeFormatter);
  }

  /**
   * localDateTime转字符串
   *
   * @param localDateTime localDateTime
   * @param pattern pattern
   * @return localDateTime字符串
   */
  public static String localDateTimeToString(LocalDateTime localDateTime, String pattern) {
    // 指定模式
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
    // 将 LocalDateTime 格式化为字符串
    return localDateTime.format(dateTimeFormatter);
  }

  /**
   * 字符串转localDateTime
   *
   * @param localDateStr localDateTime字符串
   * @param pattern 解析pattern
   * @return LocalDateTime
   */
  public static LocalDateTime stringToLocalDateTime(String localDateStr, String pattern) {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
    // 将字符串格式化为 LocalDateTime
    return LocalDateTime.parse(localDateStr, dateTimeFormatter);
  }

  /**
   * 获取两个日期直接的所有日期（开始结束日期都包含）.
   *
   * @param beginStr 开始日期字符串 yyyy-MM-dd
   * @param endStr 结束日期字符串 yyyy-MM-dd
   * @return 区间内所有的日期
   */
  public static List<LocalDate> getBetweenLocalDate(String beginStr, String endStr) {
    if (StringUtils.isBlank(beginStr) || StringUtils.isBlank(endStr)) {
      return new ArrayList<>();
    }
    LocalDate begin = LocalDate.parse(beginStr);
    LocalDate end = LocalDate.parse(endStr);
    Set<LocalDate> set = new HashSet<>();
    if (begin.isAfter(end)) {
      return new ArrayList<>();
    }
    if (begin.equals(end)) {
      set.add(begin);
      return new ArrayList<>(set);
    }
    set.add(begin);
    while ((begin = begin.plusDays(1)).isBefore(end)) {
      set.add(begin);
    }
    set.add(end);
    return new ArrayList<>(set);
  }

  /**
   * 获取两个时间区间所有的时间（分钟+1）.
   *
   * @param beginStr 开始时间字符串 00:00
   * @param endStr 结束时间字符串 00:00
   * @return 区间内所有的时间
   */
  public static List<LocalTime> getBetweenLocalTime(String beginStr, String endStr) {
    if (StringUtils.isBlank(beginStr) || StringUtils.isBlank(endStr)) {
      return new ArrayList<>();
    }
    LocalTime begin = LocalTime.parse(beginStr);
    LocalTime end = LocalTime.parse(endStr);
    begin = LocalTime.of(begin.getHour(), begin.getMinute());
    end = LocalTime.of(end.getHour(), end.getMinute());
    final LocalTime finalBegin = LocalTime.of(begin.getHour(), begin.getMinute(),
        begin.getSecond());
    Set<LocalTime> set = new HashSet<>();
    if (begin.isAfter(end)) {
      return new ArrayList<>();
    }
    if (begin.equals(end)) {
      set.add(begin);
      return new ArrayList<>(set);
    }
    set.add(begin);
    while ((begin = begin.plusMinutes(1)).isBefore(end) && !begin.equals(finalBegin)) {
      set.add(begin);
    }
    set.add(end);
    return new ArrayList<>(set);
  }

  /**
   * 获取两个时间区间所有的时间（分钟+1）.
   *
   * @param begin 开始时间
   * @param end 结束时间
   * @return 区间内所有的时间
   */
  public static List<LocalTime> getBetweenLocalTime(LocalTime begin, LocalTime end) {
    if (begin == null || end == null) {
      return new ArrayList<>();
    }
    begin = LocalTime.of(begin.getHour(), begin.getMinute());
    end = LocalTime.of(end.getHour(), end.getMinute());
    final LocalTime finalBegin = LocalTime.of(begin.getHour(), begin.getMinute(),
        begin.getSecond());
    Set<LocalTime> set = new HashSet<>();
    if (begin.isAfter(end)) {
      return new ArrayList<>();
    }
    if (begin.equals(end)) {
      set.add(begin);
      return new ArrayList<>(set);
    }
    set.add(begin);
    while ((begin = begin.plusMinutes(1)).isBefore(end) && !begin.equals(finalBegin)) {
      set.add(begin);
    }
    set.add(end);
    return new ArrayList<>(set);
  }

  /**
   * 将LocalTime转换为字符串
   */
  public static String convertLocalTimeToString(LocalTime time) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    return time.format(formatter);
  }
}
