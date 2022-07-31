package com.bulain.mybatis.plugin;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/***
 * 缓存插件-分页查询不使用缓存 
 */
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
        RowBounds.class, ResultHandler.class})})
public class PageNoCacheInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        if (args.length > 1 & args[1] instanceof Map) {
            @SuppressWarnings("unchecked")
            HashMap<String, Object> paramMap = (HashMap<String, Object>) args[1];
            boolean matches = paramMap.entrySet().stream().anyMatch(entry -> entry.getValue() instanceof Page);
            if (matches) {
                Class<?> clazz = ms.getClass();
                Field useCache = clazz.getDeclaredField("useCache");
                useCache.setAccessible(true);
                useCache.set(ms, false);
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }

}
