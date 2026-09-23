package com.company.wms.event;

import com.company.wms.domain.Warehouse;
import com.company.wms.domain.WarehouseStatus;
import com.company.wms.event.outbox.OutboxRecord;
import com.company.wms.event.outbox.OutboxRepository;
import com.company.wms.event.outbox.OutboxStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class WarehouseEventPublisher {

	private static final Logger log = LoggerFactory.getLogger(WarehouseEventPublisher.class);

	private static final String SOURCE = "warehouse-service";
	private static final String VERSION = "1.0";

	private final OutboxRepository outboxRepository;
	private final ObjectMapper objectMapper;

	public WarehouseEventPublisher(OutboxRepository outboxRepository, ObjectMapper objectMapper) {
		this.outboxRepository = outboxRepository;
		this.objectMapper = objectMapper;
	}

	public void publishCreated(Warehouse warehouse) {
		WarehouseCreatedEvent event = new WarehouseCreatedEvent(UUID.randomUUID().toString(), "WarehouseCreated",
				VERSION, Instant.now(), SOURCE, MDC.get("correlationId"), warehouse.getId().toString(),
				new WarehouseCreatedEvent.Payload(warehouse.getWarehouseCode(), warehouse.getName(),
						warehouse.getStatus().name()));
		save(event.eventId(), event.eventType(), event);
	}

	public void publishStatusChanged(Warehouse warehouse, WarehouseStatus oldStatus) {
		WarehouseStatusChangedEvent event = new WarehouseStatusChangedEvent(UUID.randomUUID().toString(),
				"WarehouseStatusChanged", VERSION, Instant.now(), SOURCE, MDC.get("correlationId"),
				warehouse.getId().toString(), new WarehouseStatusChangedEvent.Payload(warehouse.getWarehouseCode(),
						oldStatus.name(), warehouse.getStatus().name()));
		save(event.eventId(), event.eventType(), event);
	}

	private void save(String eventId, String eventType, Object payload) {
		try {
			String json = objectMapper.writeValueAsString(payload);

			OutboxRecord record = new OutboxRecord();
			record.setEventId(eventId);
			record.setEventType(eventType);
			record.setPayload(json);
			record.setCreatedAt(Instant.now());
			record.setStatus(OutboxStatus.PENDING);

			outboxRepository.save(record);
		} catch (JsonProcessingException e) {
			log.error("Failed to serialize event {}", eventType, e);
			throw new IllegalStateException("Event serialization failed", e);
		}
	}
}