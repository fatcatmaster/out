package com.fatcat.search.controller;

import com.fatcat.common.base.ResultResponse;
import com.fatcat.elastic.template.ElasticIndexTemplate;
import com.fatcat.search.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 索引管理 前端控制器
 *
 * @author fatcat
 * @description 索引管理 前端控制器
 * @create 2021/4/21
 **/
@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private ElasticIndexTemplate indexTemplate;

    /**
     * 创建索引
     *
     * @return 成功与否
     */
    @PostMapping("/createIndex")
    public ResultResponse createIndex() throws IOException {
        indexTemplate.createIndex(User.class);
        return ResultResponse.success();
    }

    /**
     * 更新索引
     */
    @PutMapping("/updateIndex")
    public ResultResponse updateIndex() throws IOException {
        indexTemplate.updateIndex(User.class);
        return ResultResponse.success();
    }

    /**
     * 删除索引
     */
    @DeleteMapping("/deleteIndex")
    public ResultResponse deleteIndex() throws IOException {
        indexTemplate.delIndex(User.class);
        return ResultResponse.success();
    }
}
