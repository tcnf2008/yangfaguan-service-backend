//package com.nau.service;
//
//import cn.hutool.core.util.ObjectUtil;
//import com.nau.common.CaptureConfig;
//import com.nau.common.JwtTokenUtils;
//import com.nau.dao.AdminDao;
//import com.nau.entity.User;
//import com.nau.entity.Admin;
//import com.nau.entity.Params;
//import com.nau.exception.CustomException;
//import com.github.pagehelper.PageHelper;
//import com.github.pagehelper.PageInfo;
//import com.wf.captcha.utils.CaptchaUtil;
//import org.springframework.beans.BeanUtils;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import java.util.List;
//
//@Service
//public class AdminService {
//
//    @Resource
//    private AdminDao adminDao;
//
//    public List<Admin> findAll() {
//        return adminDao.selectAll();
//    }
//
//    public PageInfo<Admin> findBySearch(Params params) {
//        // 开启分页查询
//        PageHelper.startPage(params.getPageNum(), params.getPageSize());
//        // 接下来的查询会自动按照当前开启的分页设置来查询
//        List<Admin> list = adminDao.findBySearch(params);
//        return PageInfo.of(list);
//    }
//
//    public void add(Admin admin) {
//        // 1. 用户名一定要有，否则不让新增（后面需要用户名登录）
//        if (admin.getUsername() == null || "".equals(admin.getUsername())) {
//            throw new CustomException("用户名不能为空");
//        }
//        // 2. 进行重复性判断，同一名字的管理员不允许重复新增：只要根据用户名去数据库查询一下就可以了
//        Admin user = adminDao.findByUserName(admin.getUsername());
//        if (user != null) {
//            // 说明已经有了，这里我们就要提示前台不允许新增了
//            throw new CustomException("该用户名已存在，请更换用户名");
//        }
//        // 初始化一个密码
//        if (admin.getPassword() == null) {
//            admin.setPassword("123456");
//        }
//        admin.setRole("ADMIN");
//        adminDao.insertSelective(admin);
//    }
//
//    public void update(Admin admin) {
//        adminDao.updateByPrimaryKeySelective(admin);
//    }
//
//    public void delete(Integer id) {
//        adminDao.deleteByPrimaryKey(id);
//    }
//
//    public User login(User admin, String key, HttpServletRequest request) {
//        // 判断验证码对不对
//        if (!admin.getVerCode().toLowerCase().equals(CaptureConfig.CAPTURE_MAP.get(key))) {
//            // 如果不相等，说明验证不通过
//            CaptchaUtil.clear(request);
//            throw new CustomException("验证码不正确");
//        }
//        CaptureConfig.CAPTURE_MAP.remove(key);
//
//        // 1. 进行一些非空判断
//        if (admin.getUsername() == null || "".equals(admin.getUsername())) {
//            throw new CustomException("用户名不能为空");
//        }
//        if (admin.getPassword() == null || "".equals(admin.getPassword())) {
//            throw new CustomException("密码不能为空");
//        }
//        // 2. 从数据库里面根据这个用户名和密码去查询对应的管理员信息，
//        Admin user = adminDao.findByNameAndPassword(admin.getUsername(), admin.getPassword());
//        if (user == null) {
//            // 如果查出来没有，那说明输入的用户名或者密码有误，提示用户，不允许登录
//            throw new CustomException("用户名或密码输入错误");
//        }
//        // 如果查出来了有，那说明确实有这个管理员，而且输入的用户名和密码都对；
//        // 生成该登录用户对应的token，然后跟着user一起返回到前台
//        String tokenData = user.getId() + "-" + user.getRole();
//        String token = JwtTokenUtils.genToken(tokenData, user.getPassword());
//        user.setToken(token);
//        return user;
//    }
//
//    public Admin findById(Integer id) {
//        return adminDao.selectByPrimaryKey(id);
//    }
//
//    /**
//     * 用户修改密码
//     * @param account
//     */
//    public void updatePassword(User account) {
//        Admin admin = adminDao.findByUserName(account.getUsername());
//        if(ObjectUtil.isNull(admin)){
//            throw new CustomException("用户不存在");
//        }
//        if(!account.getPassword().equals(admin.getPassword())){
//            throw new CustomException("原密码不正确");
//        }
//        admin.setPassword(account.getNewPassword());
//        adminDao.updateByPrimaryKey(admin);
//    }
//
//    /**
//     *修改个人信息
//     * @param account
//     */
//    public void updatePersonal(User account) {
//        Admin admin = new Admin();
//        BeanUtils.copyProperties(account,admin);
//        update(admin);
//    }
//}
