package com.nau.dao;

import com.nau.entity.Notice;
import com.nau.entity.Params;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface NoticeDao extends Mapper<Notice> {

    List<Notice> findBySearch(@Param("params") Params params);
    
    List<Notice> findAll(@Param("params") Params params);
}
