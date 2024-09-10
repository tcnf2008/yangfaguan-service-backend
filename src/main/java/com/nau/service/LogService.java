package com.nau.service;

import com.nau.dao.LogDao;
import com.nau.entity.Log;
import com.nau.entity.Params;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LogService {

    @Resource
    private LogDao logDao;
    
    /**
     * 分页查询
     * @param params
     * @return
     */
    public PageInfo<Log> findBySearch(Params params) {
        // 开启分页查询
        PageHelper.startPage(params.getPageNum(), params.getPageSize());
        // 接下来的查询会自动按照当前开启的分页设置来查询
        List<Log> list = logDao.findBySearch(params);
        return PageInfo.of(list);
    }
    
    /**
     * 添加
     * @param log
     */
    public void add(Log log) {
        logDao.insertSelective(log);
    }
    
    /**
     * 修改
     * @param log
     */
    public void update(Log log) {
        logDao.updateByPrimaryKeySelective(log);
    }
    
    /**
     * 根据id删除
     * @param id
     * @return
     */
    public void delete(Integer id) {
        logDao.deleteByPrimaryKey(id);
    }
    
}
