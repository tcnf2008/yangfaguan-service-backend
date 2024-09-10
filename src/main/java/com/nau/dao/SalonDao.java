// Copyright 2024 Haipeng.wang . All Rights Reserved.

package com.nau.dao;

import com.nau.entity.Salon;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Haipeng.wang NAU
 * @date 2024-09-04.
 */
@Repository
public interface SalonDao extends Mapper<Salon> {
  // additional methods if needed

  @Select("SELECT * FROM salon;")
  List<Salon> findAll();
}
