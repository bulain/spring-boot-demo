package com.bulain.mcp.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

@Data
@Accessors(chain = true)
@Schema(title = "单据信息")
public class BillHed {

    @Schema(title = "单号")
    private String code;

    @Schema(title = "日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Schema(title = "状态")
    private String state;

    @Schema(title = "明细")
    private List<BillDet> details;

}
