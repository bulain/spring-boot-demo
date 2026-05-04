package com.bulain.mybatis.sys.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 权限表
 * </p>
 *
 * @author Bulain
 * @since 2024-11-11
 */
@Data
@Accessors(chain = true)
@TableName("sys_permissions")
public class SysPermission {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @TableField("name")
    private String name;

    @TableField("code")
    private String code;

    @TableField("resource_type")
    private String resourceType;

    @TableField("description")
    private String description;

    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private String createdBy;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private String updatedBy;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField(value = "pubts", fill = FieldFill.INSERT_UPDATE)
    @Version
    private Long pubts;

}
