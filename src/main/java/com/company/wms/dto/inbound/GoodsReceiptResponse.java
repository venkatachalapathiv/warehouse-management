package com.company.wms.dto.inbound;

public record GoodsReceiptResponse(
        String id,
        String receiptNumber,
        String warehouseId,
        String status
) {
}