package com.bulain.mybatis.redis;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 缓存服务接口，利用此接口来访问缓存
 */
public interface RedisService {
	
	//=================缓存键操作======================
    
    /**
     * 得到缓存的键值列表
     * @param pattern 缓存键匹配模式
     * @return 匹配的缓存键
     */
    Set<String> keys(String pattern);
	
	//=================缓存形式(KEY-VALUE)======================
	/**
	 * 将数据放入缓存
	 * @param key 缓存键
	 * @param obj 缓存对象
	 * @param seconds 过期秒数
	 */
    void set(String key, Object obj, int seconds);
    
    /**
     * 根据缓存键获得缓存数据
     * @param <T> 缓存数据类型
     * @param key 缓存键
     * @return 缓存数据
     */
    <T> T get(String key);
    
    /**
          * 根据缓存键获得缓存数据列表
     * @param <T> 缓存数据类型
     * @param keys 缓存键
     * @return 缓存数据列表
     */
    <T> List<T> mget(Collection<String> keys);
    
    /**
     * 删除缓存
     * @param key 缓存键
     */
    void del(String... key);
	
    //==================哈希缓存形式(KEY-FIELD-VALUE)=====================
    /**
     * 将数据放入缓存
     * @param key 缓存键
     * @param field 哈希键
     * @param obj 缓存数据
     * @param seconds 过期秒数
     */
    void hset(String key, String field, Object obj, int seconds);
    
    /**
     * 得到对应的缓存值
     * @param <T> 缓存数据类型
     * @param key 缓存键
     * @param field 哈希键
     * @return 缓存数据
     */
    <T> T hget(String key, String field);
    
    /**
         * 得到对应的缓存值列表
     * @param <T> 缓存数据类型
     * @param key 缓存键列表
     * @param fields 哈希键
     * @return 缓存数据
     */
    <T> List<T> hmget(String key, Collection<String> fields);
    
    /**
     * 删除缓存的键值
     * @param key 缓存键
     * @param fields 哈希键
     */
    void hdel(String key, String... fields);
    
    /**
     * 返回哈希键的数量
     * @param key 缓存键
     * @return 缓存个数
     */
    Long hlen(String key);
    
    /***
        * 过期时间设置
    * @param key 缓存键
    * @param seconds 过期秒数
     */
    void expire(String key, int seconds);
    
}
