package com.company.wms.dto;

import com.company.wms.domain.WarehouseStatus;
import jakarta.validation.constraints.NotNull;

public record WarehouseStatusUpdateRequest(
        @NotNull(message = "status is required") WarehouseStatus status
) {
}