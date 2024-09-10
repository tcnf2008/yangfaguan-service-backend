package com.nau.dao;

import com.nau.entity.Salon;
import com.nau.entity.Service;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface ServiceDao extends Mapper<Service> {

  // additional methods if needed
  @Select("SELECT * FROM service;")
  List<Service> findAll();
}