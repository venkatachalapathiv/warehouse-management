package com.company.wms.dto;

import com.company.wms.domain.WarehouseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record WarehouseRequest(

        @NotBlank(message = "warehouseCode is required")
        @Pattern(regexp = "^[A-Z0-9-]{3,50}$",
                 message = "warehouseCode must match ^[A-Z0-9-]{3,50}$")
        String warehouseCode,

        @NotBlank(message = "name is required")
        @Size(max = 150, message = "name must be <= 150 characters")
        String name,

        @Size(max = 500, message = "address must be <= 500 characters")
        String address,

        WarehouseStatus status
) {
}