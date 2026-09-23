package com.company.wms.dto;

import com.company.wms.domain.WarehouseStatus;

import java.time.Instant;
import java.util.UUID;

public record WarehouseResponse(
        UUID id,
        String warehouseCode,
        String name,
        String address,
        WarehouseStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}