package com.nau.dao;

import com.nau.entity.Salon;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SalonTest {

  @Autowired
  SalonDao salonDao;

  @Test
  void testQuerySalonList() {
    List<Salon> salonList = salonDao.findAll();
    assert salonList!=null;
  }
}
