package com.bulain.mcp.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Accessors(chain = true)
@Schema(title = "库存明细")
public class InvDet {

    @Schema(title = "货品编码")
    private String code;

    @Schema(title = "货品名称")
    private String name;

    @Schema(title = "数量")
    private BigDecimal quantity;

    @Schema(title = "库位")
    private String location;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(title = "入库时间")
    private LocalDate storageTime;

}
