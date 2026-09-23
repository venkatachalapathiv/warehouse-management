package com.company.wms.event;

import java.time.Instant;

public record WarehouseCreatedEvent(
        String eventId,
        String eventType,
        String eventVersion,
        Instant occurredAt,
        String source,
        String correlationId,
        String entityId,
        Payload payload
) {
    public record Payload(
            String warehouseCode,
            String name,
            String status
    ) {
    }
}