package com.company.wms.service;

import com.company.wms.domain.WarehouseStatus;
import com.company.wms.dto.WarehouseRequest;
import com.company.wms.dto.WarehouseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface WarehouseService {

    WarehouseResponse create(WarehouseRequest request);

    WarehouseResponse findById(UUID id);

    Page<WarehouseResponse> findAll(WarehouseStatus status, Pageable pageable);

    WarehouseResponse updateStatus(UUID id, WarehouseStatus newStatus);
}