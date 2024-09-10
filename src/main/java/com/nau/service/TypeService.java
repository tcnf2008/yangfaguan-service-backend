package com.nau.service;

import com.nau.dao.RoomDao;
import com.nau.dao.TypeDao;
import com.nau.entity.Params;
import com.nau.entity.Type;
import com.nau.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TypeService {

    @Resource
    private TypeDao typeDao;
    
    @Resource
    private RoomDao roomDao;

    public List<Type> findAll() {
        return typeDao.selectAll();
    }
    
    /**
     * 分页查询
     * @param params
     * @return
     */
    public PageInfo<Type> findBySearch(Params params) {
        // 开启分页查询
        PageHelper.startPage(params.getPageNum(), params.getPageSize());
        // 接下来的查询会自动按照当前开启的分页设置来查询
        List<Type> list = typeDao.findBySearch(params);
        return PageInfo.of(list);
    }
    
    /**
     * 添加
     * @param type
     */
    public void add(Type type) {
        if (type.getName() == null || "".equals(type.getName())) {
            throw new CustomException("分类名称不能为空");
        }
        typeDao.insertSelective(type);
    }
    
    /**
     * 修改
     * @param type
     */
    public void update(Type type) {
        if (type.getName() == null || "".equals(type.getName())) {
            throw new CustomException("分类名称不能为空");
        }
        typeDao.updateByPrimaryKeySelective(type);
    }
    
    /**
     * 根据id删除
     * @param id
     * @return
     */
    public void delete(Integer id) {
        int count = roomDao.selectRoomById(id);
        if(count > 0){
            throw new CustomException("该分类已关联相关养发服务，不能删除");
        }
        typeDao.deleteByPrimaryKey(id);
    }
    
    /**
     * 根据主键id查询
     * @param id
     * @return
     */
    public Type findById(Integer id) {
        return typeDao.selectByPrimaryKey(id);
    }
}
