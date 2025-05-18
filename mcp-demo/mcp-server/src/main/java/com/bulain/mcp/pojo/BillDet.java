package com.bulain.mcp.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@Schema(title = "单据明细")
public class BillDet {

    @Schema(title = "行号")
    private Integer row;

    @Schema(title = "货品编码")
    private String code;

    @Schema(title = "货品名称")
    private String name;

    @Schema(title = "数量")
    private BigDecimal quantity;

}
