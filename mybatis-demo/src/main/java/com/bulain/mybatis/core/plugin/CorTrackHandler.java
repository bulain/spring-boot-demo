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
        String userId = "******";

        this.setFieldValByName("createdAt", now, metaObject);
        this.setFieldValByName("createdBy", userId, metaObject);
        this.setFieldValByName("updatedAt", now, metaObject);
        this.setFieldValByName("updatedBy", userId, metaObject);
        // 乐观锁初始值为当前时间戳
        strictInsertFill(metaObject, "pubts", System::currentTimeMillis, Long.class);
        // 逻辑删除初始值为0
        strictInsertFill(metaObject, "dr", () -> 0L, Long.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        String userId = "******";

        this.setFieldValByName("updatedAt", now, metaObject);
        this.setFieldValByName("updatedBy", userId, metaObject);
        // 更新时自动更新乐观锁为当前时间戳
        strictUpdateFill(metaObject, "pubts", System::currentTimeMillis, Long.class);
    }

}
