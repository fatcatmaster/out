package com.fatcat.search.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import com.fatcat.common.base.ResultResponse;
import com.fatcat.elastic.base.EsPage;
import com.fatcat.elastic.template.ElasticDocTemplate;
import com.fatcat.search.entity.User;
import com.fatcat.search.vo.UserPageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.fatcat.elastic.utils.ElasticUtil.createIndexNameByClass;


/**
 * 文档管理 前端控制器
 *
 * @author fatcat
 * @description 文档管理 前端控制器
 * @create 2021/4/21
 **/
@RestController
@RequestMapping("/doc")
public class DocController {

    @Autowired
    private ElasticDocTemplate docTemplate;

    /**
     * 测试插入指定数量的数据
     *
     * @param times 数量
     */
    @GetMapping("test/insert")
    public ResultResponse testInsert(@RequestParam("times") int times) throws Exception {
        List<User> list = new ArrayList<>(times);
        for (int i = 0; i < times; i++) {
            Long id = IdUtil.getSnowflake(1, 1).nextId();
            LocalDateTime time = LocalDateTimeUtil.now();
            User user = new User();
            user.setUserId(id);
            user.setCreateUser(id);
            user.setUpdateUser(id);
            user.setUserName(IdUtil.objectId());
            user.setUserPhone(IdUtil.fastSimpleUUID());
            user.setUserPassword(IdUtil.simpleUUID());
            user.setCreateTime(time);
            user.setUpdateTime(time);
            list.add(user);
        }
        return saveOrUpdateBatch(list);
    }

    /**
     * 查询分页
     *
     * @param req 分页请求参数
     */
    @PostMapping("/page")
    public ResultResponse page(@RequestBody UserPageVo req) throws Exception {
        EsPage<User> query = new EsPage<>();
        User user = new User();
        user.setUserId(req.getUserId());
        user.setUserName(req.getUserName());
        query.setSearch(user);
        query.setSize(req.getSize());
        query.setCurrent(req.getCurrent());
        query.setOrders(req.getOrders());
        return ResultResponse.success(docTemplate.page(query, User.class));
    }

    /**
     * 查询列表
     *
     * @param user 请求参数
     */
    @PostMapping("/list")
    public ResultResponse list(@RequestBody User user) throws Exception {
        return ResultResponse.success(docTemplate.list(user, User.class));
    }

    /**
     * 插入或更新一条文档
     *
     * @param user 文档值
     */
    @PostMapping("/saveOrUpdate")
    public ResultResponse saveOrUpdate(@RequestBody User user) throws Exception {
        docTemplate.saveOrUpdate(user);
        return ResultResponse.success(user);
    }

    /**
     * 批量插入或更新文档
     *
     * @param list 文档集合
     */
    @PostMapping("/saveOrUpdateBatch")
    public ResultResponse saveOrUpdateBatch(@RequestBody List<User> list) throws Exception {
        docTemplate.saveOrUpdateBatch(list);
        return ResultResponse.success(list);
    }

    /**
     * 根据id删除一条文档
     *
     * @param id id值
     */
    @DeleteMapping("/delDocById/{id}")
    public ResultResponse delDocById(@PathVariable("id") String id) throws Exception {
        String indexName = createIndexNameByClass(User.class);
        docTemplate.delDocById(indexName, id);
        return ResultResponse.success(id);
    }

    /**
     * 批量删除文档
     *
     * @param ids id集合
     */
    @PostMapping("/delDocBatch")
    public ResultResponse delDocBatch(@RequestBody List<String> ids) throws Exception {
        String indexName = createIndexNameByClass(User.class);
        docTemplate.delDocBatch(indexName, ids);
        return ResultResponse.success(ids);
    }

}
