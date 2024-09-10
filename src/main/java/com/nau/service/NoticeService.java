package com.nau.service;

import com.nau.dao.NoticeDao;
import com.nau.entity.Notice;
import com.nau.entity.Params;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class NoticeService {

    @Resource
    private NoticeDao noticeDao;

    public List<Notice> findAll(Params params) {
        return noticeDao.findAll(params);
    }
    
    /**
     * 分页查询
     * @param params
     * @return
     */
    public PageInfo<Notice> findBySearch(Params params) {
        // 开启分页查询
        PageHelper.startPage(params.getPageNum(), params.getPageSize());
        // 接下来的查询会自动按照当前开启的分页设置来查询
        List<Notice> list = noticeDao.findBySearch(params);
        return PageInfo.of(list);
    }
    
    /**
     * 添加通知
     * @param type
     */
    public void add(Notice type) {
        noticeDao.insertSelective(type);
    }
    
    /**
     * 修改
     * @param type
     */
    public void update(Notice type) {
        noticeDao.updateByPrimaryKeySelective(type);
    }
    
    /**
     * 根据id删除
     * @param id
     * @return
     */
    public void delete(Integer id) {
        noticeDao.deleteByPrimaryKey(id);
    }
    
    /**
     * 根据主键id查询
     * @param id
     * @return
     */
    public Notice findById(Integer id) {
        return noticeDao.selectByPrimaryKey(id);
    }
}
