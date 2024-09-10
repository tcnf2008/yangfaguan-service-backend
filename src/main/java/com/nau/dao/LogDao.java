package com.nau.dao;

import com.nau.entity.Log;
import com.nau.entity.Params;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface LogDao extends Mapper<Log> {

    List<Log> findBySearch(@Param("params") Params params);
    
    List<Log> findAll(@Param("params") Params params);
}
