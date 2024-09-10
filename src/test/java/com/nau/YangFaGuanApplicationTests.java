package com.nau;

import com.nau.dao.UserDao;
import com.nau.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class YangFaGuanApplicationTests {

  @Autowired
  UserDao userDao;

  @Test
  void contextLoads() {
    User user = userDao.selectByPrimaryKey(1);
  }

}
