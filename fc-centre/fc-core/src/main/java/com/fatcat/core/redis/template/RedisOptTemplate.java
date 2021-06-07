package com.fatcat.core.redis.template;

import cn.hutool.core.convert.ConvertException;
import cn.hutool.json.JSONUtil;
import com.fatcat.core.logger.utils.LogUtil;
import com.fatcat.core.redis.config.RedisConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 自定义redis的操作模板，统一时间单位为 “秒”
 * 自定义是为了统一处理异常，不在业务层中断业务操作
 * 目前只封装了常用的 string 类型， redis 的 hash 、list 、set 、zset(有序集合) 暂未封装
 *
 * @author fatcat
 * @description redis 的操作类
 * @create 2021/5/7
 **/
@SpringBootConfiguration
@ConditionalOnClass(RedisConfiguration.class)
public class RedisOptTemplate {

    /**
     * 注入自定义的操作模板
     */
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置时长
     *
     * @param key  键值
     * @param time 时长，单位秒
     */
    public Boolean expire(Object key, long time) {
        try {
            String optKey = String.valueOf(key);
            return redisTemplate.expire(optKey, time, TimeUnit.SECONDS);
        } catch (Exception ex) {
            LogUtil.error("redis 设置过期时间失败", ex);
            return Boolean.FALSE;
        }
    }

    /**
     * 获取指定键值的过期剩余时间
     *
     * @param key 键值
     */
    public Long getExpire(Object key) {
        try {
            String optKey = String.valueOf(key);
            return redisTemplate.getExpire(optKey, TimeUnit.SECONDS);
        } catch (Exception ex) {
            LogUtil.error("redis 获取过期时间失败", ex);
            return null;
        }
    }

    /**
     * 返回是否存在当前键值
     *
     * @param key 键值
     */
    public Boolean hasKey(Object key) {
        try {
            String optKey = String.valueOf(key);
            return redisTemplate.hasKey(optKey);
        } catch (Exception ex) {
            LogUtil.error("redis 判断指定键是否存在失败", ex);
            return Boolean.FALSE;
        }
    }

    /**
     * 保存一个键值对，时长默认永久
     *
     * @param key   键
     * @param value 值
     */
    public Boolean set(Object key, Object value) {
        try {
            String optKey = String.valueOf(key);
            redisTemplate.opsForValue().set(optKey, value);
            return Boolean.TRUE;
        } catch (Exception ex) {
            LogUtil.error("redis 保存键值失败", ex);
            return Boolean.FALSE;
        }
    }

    /**
     * 保存一个键值对，用户时长指定
     *
     * @param key   键
     * @param value 值
     * @param time  时长，单位秒
     */
    public Boolean set(Object key, Object value, long time) {
        try {
            String optKey = String.valueOf(key);
            redisTemplate.opsForValue().set(optKey, value, time, TimeUnit.SECONDS);
            return Boolean.TRUE;
        } catch (Exception ex) {
            LogUtil.error("redis 保存键值失败", ex);
            return Boolean.FALSE;
        }
    }


    /**
     * 获取指定键值的值
     *
     * @param key 键值
     */
    public Object get(Object key) {
        try {
            String optKey = String.valueOf(key);
            return redisTemplate.opsForValue().get(optKey);
        } catch (Exception ex) {
            LogUtil.error("redis 获取指定键的值失败", ex);
            return null;
        }
    }

    /**
     * 当且仅当保存的数据是用户自己手动序列化后的json格式
     *
     * @param key   键值
     * @param clazz 指定对象类
     */
    public <T> T get(Object key, Class<T> clazz) {
        try {
            String optKey = String.valueOf(key);
            Object value = redisTemplate.opsForValue().get(optKey);
            return JSONUtil.toBean(String.valueOf(value), clazz);
        } catch (ConvertException ce) {
            LogUtil.error("redis 获取指定键的值, 转换失败", ce);
            return null;
        } catch (Exception ex) {
            LogUtil.error("redis 获取指定键的值, 获取失败", ex);
            return null;
        }
    }

    /**
     * 删除一组键
     *
     * @param keys 键集合
     */
    public Boolean del(Object... keys) {
        try {
            List<String> optKeys = Arrays.stream(keys)
                    .map(String::valueOf)
                    .collect(Collectors.toList());
            redisTemplate.delete(optKeys);
            return Boolean.TRUE;
        } catch (Exception ex) {
            LogUtil.error("redis 删除键值失败", ex);
            return Boolean.FALSE;
        }
    }

    /**
     * 自增 1
     *
     * @param key 键
     */
    public Object increment(Object key) {
        try {
            String optKey = String.valueOf(key);
            return redisTemplate.opsForValue().increment(optKey);
        } catch (Exception ex) {
            LogUtil.error("redis 自增失败", ex);
            return null;
        }
    }

    /**
     * 自增指定值
     *
     * @param key   键
     * @param delta 指定值
     */
    public Object increment(Object key, Object delta) {
        try {
            String optKey = String.valueOf(key);
            String deltaStr = String.valueOf(delta);
            if (delta instanceof Short
                    || delta instanceof Integer
                    || delta instanceof Long) {
                return redisTemplate.opsForValue().increment(optKey, Long.parseLong(deltaStr));
            } else if (delta instanceof Float
                    || delta instanceof Double) {
                return redisTemplate.opsForValue().increment(optKey, Double.parseDouble(deltaStr));
            } else {
                LogUtil.warn("不支持当前数据类型");
                return null;
            }
        } catch (Exception ex) {
            LogUtil.error("redis 自增失败", ex);
            return null;
        }
    }

    /**
     * 自减 1
     *
     * @param key 键
     */
    public Object decrement(Object key) {
        try {
            String optKey = String.valueOf(key);
            return redisTemplate.opsForValue().decrement(optKey);
        } catch (Exception ex) {
            LogUtil.error("redis 自减失败", ex);
            return null;
        }
    }

    /**
     * 自减指定值
     *
     * @param key   键
     * @param delta 指定值
     */
    public Object decrement(Object key, Object delta) {
        try {
            String optKey = String.valueOf(key);
            String deltaStr = String.valueOf(delta);
            if (delta instanceof Short
                    || delta instanceof Integer
                    || delta instanceof Long) {
                return redisTemplate.opsForValue().decrement(optKey, Long.parseLong(deltaStr));
            } else {
                LogUtil.warn("不支持当前数据类型");
                return null;
            }
        } catch (Exception ex) {
            LogUtil.error("redis 自减失败", ex);
            return null;
        }
    }
}
