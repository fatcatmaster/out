package com.fatcat.user.app.controller;

import com.fatcat.common.base.ResultResponse;
import com.fatcat.common.exception.FatCatException;
import com.fatcat.core.logger.utils.LogUtil;
import com.fatcat.user.api.vo.TestVo;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 自我测试 前端控制器
 *
 * @author fatcat
 * @description 自我测试 前端控制器
 * @create 2021/4/13
 **/
@RestController
@RequestMapping("/test")
public class TestController {

    /**
     * 验证Post @Valid 的异常返回结果
     *
     * @param vo @Valid 的校验实体
     * @return
     */
    @PostMapping("/postValidated")
    public ResultResponse postValidated(@RequestBody @Valid TestVo vo) {
        return ResultResponse.success(vo);
    }

    /**
     * 验证Get @Valid 的异常返回结果
     *
     * @param vo @Valid 的校验实体
     * @return
     */
    @GetMapping("/getValidated")
    public ResultResponse getValidated(@Valid TestVo vo) {
        return ResultResponse.success(vo);
    }


    /**
     * 验证 @RequestParam 的异常返回结果
     *
     * @param id 请求参数
     * @return
     */
    @GetMapping("/requestParam")
    public ResultResponse requestParam(@RequestParam(value = "id") Long id,
                                       @RequestParam(value = "name") String name) {
        return ResultResponse.success(id);
    }

    /**
     * 验证自定义异常的返回结果
     *
     * @return
     */
    @GetMapping("/myselfException")
    public ResultResponse myselfException() {
        throw new FatCatException();
    }

    /**
     * 验证非包装类型的返回结果
     *
     * @param vo 任意参数
     * @return
     */
    @GetMapping("/returnResult")
    public TestVo returnResult(TestVo vo) {
        return vo;
    }

    /**
     * 验证代码错误异常返回结果
     */
    @GetMapping("/codeError")
    public void codeError() {
        int var = 1 / 0;
    }

    /**
     * 验证 x-www-form-urlencoded 格式的请求
     *
     * @param vo 请求数据
     */
    @PostMapping("/post")
    public void post(@Valid TestVo vo) {

    }

    /**
     * 验证put请求
     *
     * @param id 测试id
     * @param vo 测试请求体
     */
    @PutMapping("/put/{id}")
    public void put(@PathVariable("id") Long id,
                    @Valid TestVo vo) {

    }

    /**
     * 验证delete请求
     *
     * @param id   测试id
     * @param name 测试姓名
     */
    @DeleteMapping("/delete/{id}/{name}")
    public void delete(@PathVariable("id") Long id,
                       @PathVariable("name") String name) {
        LogUtil.debug("debug");
        LogUtil.info("info");
        LogUtil.error("error");
    }

    /**
     * 测试返回字符串
     */
    @GetMapping("/returnString")
    public String returnString() {
        return "This is a string";
    }
}
