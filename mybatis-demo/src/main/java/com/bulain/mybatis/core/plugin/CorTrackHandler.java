package com.bulain.mybatis.core.plugin;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自定义元数据拦截处理（行级）
 */
@Component
public class CorTrackHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        String userId = "1234567890";

        this.setFieldValByName("createdAt", now, metaObject);
        this.setFieldValByName("createdBy", userId, metaObject);
        this.setFieldValByName("updatedAt", now, metaObject);
        this.setFieldValByName("updatedBy", userId, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        String userId = null;

        this.setFieldValByName("updatedAt", now, metaObject);
        this.setFieldValByName("updatedBy", userId, metaObject);
    }

}
