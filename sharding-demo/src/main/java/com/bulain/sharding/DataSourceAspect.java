package com.bulain.sharding;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;

@Aspect
@Configuration
@Slf4j
@Order(0)
public class DataSourceAspect {

    @Pointcut("execution(* com.bulain.sharding..*(..)) && @annotation(com.bulain.sharding.ShardingDs)")
    public void dataSourceAspect() {
    }

    @Around("dataSourceAspect()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Class<?> aClass = joinPoint.getTarget().getClass();
        ShardingDs cds = aClass.getAnnotation(ShardingDs.class);
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ShardingDs mds = method.getAnnotation(ShardingDs.class);
        if (mds != null || cds != null) {
            DynamicDataSource.setDataSourceName("sharding");
        }
        try {
            return joinPoint.proceed();
        } finally {
            DynamicDataSource.clearDataSource();
        }
    }

}
