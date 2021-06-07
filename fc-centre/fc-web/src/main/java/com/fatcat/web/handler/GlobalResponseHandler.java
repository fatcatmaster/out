package com.fatcat.web.handler;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import com.fatcat.common.base.ResultResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;

/**
 * 所有返回结果处理器，务必注明泛型接收
 * 数据封装只拦截app下的数据，放开feign的数据
 *
 * @author fatcat
 * @description 全局返回体处理器
 * @create 2021/4/12
 **/
@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice {

    private final static String FILTER_PACKAGE_REGEX = "com.fatcat.*.controller.*";

    /**
     * 判断是否执行 beforeBodyWrite 方法，即是否需要封装不正确的格式体
     * TODO 待完善
     *
     * @param converterType 数据解码器
     */
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        Method method = returnType.getMethod();
        // TODO 不封装返回 String 类型的返回结果，否则会报转换错误，原因未知
        if (ObjectUtil.isNotNull(method)
                && String.class.equals(method.getReturnType())) {
            return Boolean.FALSE;
        }
        // 获取包路径，封装 com.fatcat 下所有 controller 下所有方法
        String packagePath = returnType.getDeclaringClass().getName();
        if (!ReUtil.isMatch(FILTER_PACKAGE_REGEX, packagePath)) {
            return Boolean.FALSE;
        }
        // 拦截所有指定包下的返回结果
        return Boolean.TRUE;
    }

    /**
     * 修改响应体，返回统一的结果
     */
    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        // 返回结果没有封装则封装成统一返回体
        return body instanceof ResultResponse ? body : ResultResponse.success(body);
    }
}
