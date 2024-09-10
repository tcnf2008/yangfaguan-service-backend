package com.nau.service;

import com.nau.dao.RoomDao;
import com.nau.entity.Params;
import com.nau.entity.Room;
import com.nau.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RoomService {

    @Resource
    private RoomDao roomDao;

    public List<Room> findAll() {
        return roomDao.findAll();
    }
    
    /**
     * 分页查询
     * @param params
     * @return
     */
    public PageInfo<Room> findBySearch(Params params) {
        // 开启分页查询
        PageHelper.startPage(params.getPageNum(), params.getPageSize());
        // 接下来的查询会自动按照当前开启的分页设置来查询
        List<Room> list = roomDao.findBySearch(params);
        return PageInfo.of(list);
    }
    
    /**
     * 添加
     * @param room
     */
    public void add(Room room) {
        if (room.getName() == null || "".equals(room.getName())) {
            throw new CustomException("名称不能为空");
        }
        room.setStatus(0);//新添加的养发服务没有被预约就是默认为空闲中0
        roomDao.insertSelective(room);
    }
    
    /**
     * 修改
     * @param room
     */
    public void update(Room room) {
        if (room.getName() == null || "".equals(room.getName())) {
            throw new CustomException("分类名称不能为空");
        }
        roomDao.updateByPrimaryKeySelective(room);
    }
    
    /**
     * 根据id删除
     * @param id
     * @return
     */
    public void delete(Integer id) {
        roomDao.deleteByPrimaryKey(id);
    }
    
    /**
     * 根据id查询
     * @param id
     * @return
     */
    public Room findById(Integer id) {
        return roomDao.selectByPrimaryKey(id);
    }
}
