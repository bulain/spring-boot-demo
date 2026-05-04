package com.bulain.mybatis.sys.annotation;

import java.lang.annotation.*;

/**
 * 角色校验注解
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresRole {

    /**
     * 需要的角色编码
     */
    String value() default "";

}
