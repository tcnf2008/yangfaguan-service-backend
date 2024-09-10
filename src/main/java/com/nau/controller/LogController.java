package com.nau.controller;

import com.nau.common.Result;
import com.nau.entity.Log;
import com.nau.entity.Params;
import com.nau.service.LogService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/log")
public class LogController {

    private static final Logger log = LoggerFactory.getLogger(LogController.class);

    @Autowired
    private LogService logService;

    @GetMapping("/search")
    public Result findBySearch(Params params) {
        log.info("拦截器已放行，正式调用接口内部，查询系统日志信息");
        PageInfo<Log> info = logService.findBySearch(params);
        return Result.success(info);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        logService.delete(id);
        return Result.success();
    }
    
}
