package com.bulain.mybatis.demo.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("orders")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("created_by")
    private String createdBy;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    @TableField("updated_by")
    private String updatedBy;

    @TableField("version")
    @Version
    private Long version;

    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    @TableField("archived")
    private String archived;

}
