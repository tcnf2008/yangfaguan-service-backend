package com.nau.controller;

import com.nau.common.LogAround;
import com.nau.common.Result;
import com.nau.entity.Params;
import com.nau.entity.Type;
import com.nau.service.TypeService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/type")
public class TypeController {

    private static final Logger log = LoggerFactory.getLogger(TypeController.class);

    @Autowired
    private TypeService typeService;
    
    @GetMapping("/findAll")
    public Result findAll() {
        List<Type> list = typeService.findAll();
        return Result.success(list);
    }

    @PostMapping
    @LogAround("增加或修改养发服务分类信息")
    public Result save(@RequestBody Type type) {
        if (type.getId() == null) {
            typeService.add(type);
        } else {
            typeService.update(type);
        }
        return Result.success();
    }

    @GetMapping("/search")
    public Result findBySearch(Params params) {
        log.info("拦截器已放行，正式调用接口内部，查询养发服务分类信息");
        PageInfo<Type> info = typeService.findBySearch(params);
        return Result.success(info);
    }

    @DeleteMapping("/{id}")
    @LogAround("删除了一条养发服务分类信息")
    public Result delete(@PathVariable Integer id) {
        typeService.delete(id);
        return Result.success();
    }
    
}
