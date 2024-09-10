package com.nau.dao;

import com.nau.entity.Params;
import com.nau.entity.Reserve;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface ReserveDao extends Mapper<Reserve> {

    List<Reserve> findBySearch(@Param("params") Params params);
}
