package com.bulain.mcp.service;

import com.bulain.mcp.pojo.BillDet;
import com.bulain.mcp.pojo.BillHed;
import com.bulain.mcp.pojo.InvDet;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class McpServerService {

    @Tool(name = "query_asn_state", description = "查询入库单状态")
    public BillHed queryAsnState(@ToolParam(description = "入库单号") String code) {
        List<BillDet> details = new ArrayList<>();

        details.add(new BillDet()
                .setRow(1)
                .setCode("SKU4002")
                .setName("电子元件A型")
                .setQuantity(new BigDecimal("10"))
        );
        details.add(new BillDet()
                .setRow(2)
                .setCode("SKU4003")
                .setName("包装材料B型")
                .setQuantity(new BigDecimal("5"))
        );

        BillHed hed = new BillHed()
                .setCode(code)
                .setDate(LocalDate.now())
                .setState("完全上架")
                .setDetails(details);

        return hed;
    }

    @Tool(name = "query_so_state", description = "查询出库单状态")
    public BillHed querySoState(@ToolParam(description = "出库单号") String code) {
        List<BillDet> details = new ArrayList<>();

        details.add(new BillDet()
                .setRow(1)
                .setCode("SKU4002")
                .setName("电子元件A型")
                .setQuantity(new BigDecimal("10"))
        );
        details.add(new BillDet()
                .setRow(2)
                .setCode("SKU4003")
                .setName("包装材料B型")
                .setQuantity(new BigDecimal("5"))
        );

        BillHed hed = new BillHed()
                .setCode(code)
                .setDate(LocalDate.now())
                .setState("完全发货")
                .setDetails(details);

        return hed;
    }

    @Tool(name = "query_inv_state", description = "查询库存状态")
    public List<InvDet> queryInvState(@ToolParam(description = "货品编码") String code,
                                 @ToolParam(description = "库位", required = false) String location) {

        List<InvDet> details = new ArrayList<>();

        if (StringUtils.isNotBlank(location)) {
            details.add(new InvDet()
                    .setCode(code)
                    .setName("电子元件A型")
                    .setQuantity(new BigDecimal("15"))
                    .setLocation(location)
                    .setStorageTime(LocalDate.now().minusDays(5))
            );
        } else {
            details.add(new InvDet()
                    .setCode(code)
                    .setName("电子元件A型")
                    .setQuantity(new BigDecimal("10"))
                    .setLocation("A1-01-10")
                    .setStorageTime(LocalDate.now().minusDays(5))
            );
            details.add(new InvDet()
                    .setCode(code)
                    .setName("电子元件A型")
                    .setQuantity(new BigDecimal("5"))
                    .setLocation("A1-01-20")
                    .setStorageTime(LocalDate.now().minusDays(7))
            );
        }

        return details;
    }

}
