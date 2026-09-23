package com.company.wms.config;

import com.company.wms.event.outbox.OutboxRecord;
import com.company.wms.event.outbox.OutboxRepository;
import com.company.wms.event.outbox.OutboxStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Component
public class OutboxConfig {

	private static final Logger log = LoggerFactory.getLogger(OutboxConfig.class);

	private final OutboxRepository outboxRepository;

	public OutboxConfig(OutboxRepository outboxRepository) {
		this.outboxRepository = outboxRepository;
	}

	@Scheduled(fixedDelayString = "${wms.outbox.poll-interval-ms:2000}")
	@Transactional
	public void pollAndPublish() {
		List<OutboxRecord> pending = outboxRepository.findTop100ByStatusOrderByCreatedAtAsc(OutboxStatus.PENDING);

		if (pending.isEmpty()) {
			return;
		}

		for (OutboxRecord record : pending) {

			log.info("Outbox event captured (Kafka disabled): eventId={}, payload={}", record.getEventId(),
					record.getPayload());

			record.setStatus(OutboxStatus.PUBLISHED);
			record.setPublishedAt(Instant.now());
			outboxRepository.save(record);
		}
	}
}