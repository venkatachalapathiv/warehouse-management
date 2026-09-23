package com.company.wms.dto.inbound;

import java.util.List;

public record GoodsReceiptRequest(
        String warehouseId,
        String referenceType,
        String referenceId,
        List<Line> lines
) {
    public record Line(
            String skuId,
            Integer expectedQuantity,
            Integer receivedQuantity,
            String uom
    ) {
    }
}