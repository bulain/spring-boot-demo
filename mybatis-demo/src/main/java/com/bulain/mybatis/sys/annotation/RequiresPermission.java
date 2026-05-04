package com.bulain.mybatis.sys.annotation;

import java.lang.annotation.*;

/**
 * 权限校验注解
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresPermission {

    /**
     * 需要的权限编码
     */
    String value() default "";

}
