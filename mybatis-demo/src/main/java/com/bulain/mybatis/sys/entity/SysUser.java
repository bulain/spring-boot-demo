package com.bulain.mybatis.sys.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author Bulain
 * @since 2024-11-11
 */
@Data
@Accessors(chain = true)
@TableName("sys_users")
public class SysUser {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("name")
    private String name;

    @TableField("email")
    private String email;

    @TableField("phone")
    private String phone;

    @TableField("wechat_openid")
    private String wechatOpenid;

    @TableField("dingtalk_userid")
    private String dingtalkUserid;

    @TableField("status")
    private Integer status;

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

    @TableField("dr")
    @TableLogic
    private Long dr;

}
