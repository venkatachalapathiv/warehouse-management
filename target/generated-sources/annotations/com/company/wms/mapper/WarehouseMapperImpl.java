package com.company.wms.mapper;

import com.company.wms.domain.Warehouse;
import com.company.wms.domain.WarehouseStatus;
import com.company.wms.dto.WarehouseRequest;
import com.company.wms.dto.WarehouseResponse;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-24T11:15:58+0530",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260826-1225, environment: Java 25.0.4.1 (Eclipse Adoptium)"
)
@Component
public class WarehouseMapperImpl implements WarehouseMapper {

    @Override
    public Warehouse toEntity(WarehouseRequest request) {
        if ( request == null ) {
            return null;
        }

        Warehouse warehouse = new Warehouse();

        warehouse.setWarehouseCode( request.warehouseCode() );
        warehouse.setName( request.name() );
        warehouse.setAddress( request.address() );
        warehouse.setStatus( request.status() );

        return warehouse;
    }

    @Override
    public WarehouseResponse toResponse(Warehouse entity) {
        if ( entity == null ) {
            return null;
        }

        UUID id = null;
        String warehouseCode = null;
        String name = null;
        String address = null;
        WarehouseStatus status = null;
        Instant createdAt = null;
        Instant updatedAt = null;

        id = entity.getId();
        warehouseCode = entity.getWarehouseCode();
        name = entity.getName();
        address = entity.getAddress();
        status = entity.getStatus();
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();

        WarehouseResponse warehouseResponse = new WarehouseResponse( id, warehouseCode, name, address, status, createdAt, updatedAt );

        return warehouseResponse;
    }

    @Override
    public List<WarehouseResponse> toResponseList(List<Warehouse> entities) {
        if ( entities == null ) {
            return null;
        }

        List<WarehouseResponse> list = new ArrayList<WarehouseResponse>( entities.size() );
        for ( Warehouse warehouse : entities ) {
            list.add( toResponse( warehouse ) );
        }

        return list;
    }
}
