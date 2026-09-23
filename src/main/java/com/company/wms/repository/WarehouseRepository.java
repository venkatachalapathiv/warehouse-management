package com.company.wms.repository;

import com.company.wms.domain.Warehouse;
import com.company.wms.domain.WarehouseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, UUID> {

    boolean existsByWarehouseCode(String warehouseCode);

    Optional<Warehouse> findByWarehouseCode(String warehouseCode);

    Page<Warehouse> findAllByStatus(WarehouseStatus status, Pageable pageable);
}