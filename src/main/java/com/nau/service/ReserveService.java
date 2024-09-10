package com.nau.service;

import cn.hutool.core.date.DateUtil;
import com.nau.common.Result;
import com.nau.dao.NoticeDao;
import com.nau.dao.ReserveDao;
import com.nau.dao.RoomDao;
import com.nau.entity.Notice;
import com.nau.entity.Params;
import com.nau.entity.Reserve;
import com.nau.entity.Room;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ReserveService {
    
    @Resource
    private ReserveDao reserveDao;
    @Resource
    private RoomDao roomDao;
    @Autowired
    private NoticeDao noticeDao;
    
    public List<Reserve> findAll() {
        return reserveDao.selectAll();
    }
    
    /**
     * 分页查询
     *
     * @param params
     * @return
     */
    public PageInfo<Reserve> findBySearch(Params params) {
        // 开启分页查询
        PageHelper.startPage(params.getPageNum(), params.getPageSize());
        // 接下来的查询会自动按照当前开启的分页设置来查询
        List<Reserve> list = reserveDao.findBySearch(params);
        return PageInfo.of(list);
    }
    
    /**
     * 顾客预约成功插入一条记录
     *
     * @param room
     */
    public Result add(Room room) {
        if (room.getStatus() != 0) {
            return Result.error("该养发服务已被预约");
        }
        room.setStatus(1);//修改养发服务信息的状态为1使用中
        roomDao.updateByPrimaryKeySelective(room);
        //给预约记录插入一条信息
        Reserve reserve = new Reserve();
        reserve.setName(room.getName());
        reserve.setStudentName(room.getStudentName());
        reserve.setTeacherName(room.getTeacherName());
        reserve.setTime(DateUtil.now());
        reserve.setUseTime(room.getStart() + "-" +room.getEnd());
        reserve.setReserveStatus(0);//预约状态为待审核0，1审核通过，2审核不通过
        reserve.setUseStatus(0);//预约状态为待审核0，1使用中，2审核不通过，3已结束
        reserve.setRoomId(room.getId());
        reserveDao.insertSelective(reserve);
        return Result.success();
    }
    
    /**
     * 管理员点击通过按钮修改状态
     *
     * @param reserve
     */
    public void update1(Reserve reserve) {
        //通过则是预约成功，插入一条预约成功的通知信息
        Notice notice = new Notice();
        notice.setName(reserve.getStudentName());
        notice.setDescription("预约" + reserve.getName() + "养发服务成功，请留意预约时间进行使用");
        notice.setTime(DateUtil.now());
        noticeDao.insertSelective(notice);
        
        reserve.setReserveStatus(1);//审核通过，预约状态设为1审核通过
        reserve.setUseStatus(1);//审核通过，使用状态设为1审核通过为使用中
        reserveDao.updateByPrimaryKeySelective(reserve);
    }
    
    /**
     * 管理员点击不通过按钮修改状态
     *
     * @param reserve
     */
    public void update2(Reserve reserve) {
        //审核不通过则是预约失败，插入一条预约失败的通知信息
        Notice notice = new Notice();
        notice.setName(reserve.getStudentName());
        notice.setDescription("预约" + reserve.getName() + "养发服务失败，管理员审核不通过");
        notice.setTime(DateUtil.now());
        noticeDao.insertSelective(notice);
        
        reserve.setReserveStatus(2);//审核不通过，预约状态设为2审核不通过
        reserve.setUseStatus(2);//审核不通过，使用状态设为2审核不通过
        reserveDao.updateByPrimaryKeySelective(reserve);
        //审核不通过，将该养发服务的状态改为空闲中，释放出去给其他用户进行预约
        roomDao.updateById(reserve.getRoomId());
    }
    
    /**
     * 管理员或者顾客点击结束使用按钮修改状态
     *
     * @param reserve
     */
    public void update3(Reserve reserve) {
        reserve.setUseStatus(3);//结束使用，使用状态设为3
        reserveDao.updateByPrimaryKeySelective(reserve);
        //将该养发服务的状态改为空闲中，释放出去给其他用户进行预约
        roomDao.updateById(reserve.getRoomId());
    }
    
    /**
     * 顾客点击取消预约按钮修改状态
     *
     * @param reserve
     */
    public void update4(Reserve reserve) {
        reserve.setReserveStatus(1);//审核通过，预约状态设为1审核通过
        reserve.setUseStatus(3);//使用状态设为3，已结束
        reserveDao.updateByPrimaryKeySelective(reserve);
        //顾客取消预约，将该养发服务的状态改为空闲中，释放出去给其他用户进行预约
        roomDao.updateById(reserve.getRoomId());
    }
    
    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    public void delete(Integer id) {
        reserveDao.deleteByPrimaryKey(id);
    }
    
    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public Reserve findById(Integer id) {
        return reserveDao.selectByPrimaryKey(id);
    }
    
    
}
