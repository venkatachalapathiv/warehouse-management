package com.company.wms.service;

import com.company.wms.client.InboundServiceClient;
import com.company.wms.domain.Warehouse;
import com.company.wms.domain.WarehouseStatus;
import com.company.wms.dto.WarehouseRequest;
import com.company.wms.dto.WarehouseResponse;
import com.company.wms.dto.inbound.GoodsReceiptRequest;
import com.company.wms.dto.inbound.GoodsReceiptResponse;
import com.company.wms.exception.DuplicateResourceException;
import com.company.wms.exception.ResourceNotFoundException;
import com.company.wms.mapper.WarehouseMapper;
import com.company.wms.repository.WarehouseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class WarehouseServiceImpl implements WarehouseService {

    private static final Logger log = LoggerFactory.getLogger(WarehouseServiceImpl.class);

    private final WarehouseRepository repository;
    private final WarehouseMapper mapper;
    private final InboundServiceClient inboundServiceClient;

    public WarehouseServiceImpl(WarehouseRepository repository,
                                WarehouseMapper mapper,
                                InboundServiceClient inboundServiceClient) {
        this.repository = repository;
        this.mapper = mapper;
        this.inboundServiceClient = inboundServiceClient;
    }

    @Override
    public WarehouseResponse create(WarehouseRequest request) {
        if (repository.existsByWarehouseCode(request.warehouseCode())) {
            throw new DuplicateResourceException(
                    "Warehouse code already exists: " + request.warehouseCode());
        }

        Warehouse entity = mapper.toEntity(request);
        if (entity.getStatus() == null) {
            entity.setStatus(WarehouseStatus.ACTIVE);
        }
        Warehouse saved = repository.save(entity);
        log.info("Warehouse created: id={}, code={}", saved.getId(), saved.getWarehouseCode());

        // Synchronous call to inbound-service
        notifyInbound(saved);

        return mapper.toResponse(saved);
    }

    private void notifyInbound(Warehouse warehouse) {
        try {
            GoodsReceiptRequest req = new GoodsReceiptRequest(
                    warehouse.getId().toString(),
                    "PURCHASE_ORDER",
                    "PO-10001",
                    List.of(new GoodsReceiptRequest.Line(
                            "SKU-1001", 100, 100, "EA"))
            );
            GoodsReceiptResponse resp = inboundServiceClient.createGoodsReceipt(req);
            log.info("Inbound notified: receiptId={}, status={}",
                    resp.id(), resp.status());
        } catch (Exception e) {
            // Do NOT fail warehouse creation if inbound is down
            log.warn("Failed to notify inbound-service for warehouse {}: {}",
                    warehouse.getId(), e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public WarehouseResponse findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WarehouseResponse> findAll(WarehouseStatus status, Pageable pageable) {
        Page<Warehouse> page = (status == null)
                ? repository.findAll(pageable)
                : repository.findAllByStatus(status, pageable);
        return page.map(mapper::toResponse);
    }

    @Override
    public WarehouseResponse updateStatus(UUID id, WarehouseStatus newStatus) {
        Warehouse entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", id));
        if (entity.getStatus() == newStatus) {
            return mapper.toResponse(entity);
        }
        WarehouseStatus oldStatus = entity.getStatus();
        entity.setStatus(newStatus);
        log.info("Warehouse status changed: id={}, {} -> {}", id, oldStatus, newStatus);
        return mapper.toResponse(entity);
    }
}