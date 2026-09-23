package com.company.wms.event.outbox;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "outbox_event")
public class OutboxRecord {

	@Id
	@Column(name = "event_id", nullable = false, length = 50)
	private String eventId;

	@Column(name = "event_type", nullable = false, length = 100)
	private String eventType;

	@Column(name = "payload", nullable = false, columnDefinition = "text")
	private String payload;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@Column(name = "published_at")
	private Instant publishedAt;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 20)
	private OutboxStatus status;

	//  No-arg constructor
	public OutboxRecord() {
	}

	public OutboxRecord(String eventId, String eventType, String payload, Instant createdAt, Instant publishedAt,
			OutboxStatus status) {
		this.eventId = eventId;
		this.eventType = eventType;
		this.payload = payload;
		this.createdAt = createdAt;
		this.publishedAt = publishedAt;
		this.status = status;
	}

	public String getEventId() {
		return eventId;
	}

	public String getEventType() {
		return eventType;
	}

	public String getPayload() {
		return payload;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getPublishedAt() {
		return publishedAt;
	}

	public OutboxStatus getStatus() {
		return status;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public void setPublishedAt(Instant publishedAt) {
		this.publishedAt = publishedAt;
	}

	public void setStatus(OutboxStatus status) {
		this.status = status;
	}
}