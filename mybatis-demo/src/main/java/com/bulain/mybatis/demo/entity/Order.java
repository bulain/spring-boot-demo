package com.bulain.mybatis.demo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author Bulain
 * @since 2021-10-18
 */
@Data
@Accessors(chain = true)
@TableName("demo_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @TableField("order_no")
    private String orderNo;

    @TableField("extn_ref_no1")
    private String extnRefNo1;

    @TableField("extn_ref_no2")
    private String extnRefNo2;

    @TableField("extn_ref_no3")
    private String extnRefNo3;

    @TableField("created_via")
    private String createdVia;

    @TableField("remarks")
    private String remarks;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private String createdBy;

    @TableField(value = "updated_at")
    private LocalDateTime updatedAt;

    @TableField(value = "updated_by")
    private String updatedBy;

    @TableField(value ="version", fill = FieldFill.INSERT_UPDATE)
    @Version
    private Long version;

    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    @TableField("archived")
    private String archived;


}
