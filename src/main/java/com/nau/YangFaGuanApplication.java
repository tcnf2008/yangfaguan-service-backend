package com.nau;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.nau.dao")
public class YangFaGuanApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(YangFaGuanApplication.class, args);
    }
    
}
