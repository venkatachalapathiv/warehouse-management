package com.company.wms.event.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxRepository extends JpaRepository<OutboxRecord, String> {

    List<OutboxRecord> findTop100ByStatusOrderByCreatedAtAsc(OutboxStatus status);
}