package com.company.wms.controller;

import com.company.wms.domain.WarehouseStatus;
import com.company.wms.dto.WarehouseRequest;
import com.company.wms.dto.WarehouseResponse;
import com.company.wms.dto.WarehouseStatusUpdateRequest;
import com.company.wms.service.WarehouseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/warehouses")
@Validated
@Tag(name = "Warehouses", description = "Warehouse master APIs")
public class WarehouseController {

    private final WarehouseService service;

    public WarehouseController(WarehouseService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('WMS_ADMIN')")
    public ResponseEntity<WarehouseResponse> create(@Valid @RequestBody WarehouseRequest request) {
        WarehouseResponse body = service.create(request);
        return ResponseEntity.created(URI.create("/api/v1/warehouses/" + body.id())).body(body);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('WMS_ADMIN','WMS_USER')")
    public WarehouseResponse get(@PathVariable UUID id) {
        return service.findById(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('WMS_ADMIN','WMS_USER')")
    public Page<WarehouseResponse> list(
            @RequestParam(required = false) WarehouseStatus status,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return service.findAll(status, pageable);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('WMS_ADMIN')")
    public WarehouseResponse updateStatus(@PathVariable UUID id,
                                          @Valid @RequestBody WarehouseStatusUpdateRequest request) {
        return service.updateStatus(id, request.status());
    }
}