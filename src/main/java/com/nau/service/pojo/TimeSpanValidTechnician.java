// Copyright 2024 Haipeng.wang . All Rights Reserved.

package com.nau.service.pojo;

import com.nau.entity.User;
import lombok.Data;

import java.util.List;

/**
 * 时间段可用技师信息
 * @author Haipeng.wang NAU
 * @date 2024-09-05.
 */
@Data
public class TimeSpanValidTechnician {

  private String timeOfDay;
  private boolean valid;

  /**
   * 可用技师
   */
  private List<User> validTechnicians;
}
