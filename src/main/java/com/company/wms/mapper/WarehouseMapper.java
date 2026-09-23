package com.company.wms.mapper;

import com.company.wms.domain.Warehouse;
import com.company.wms.dto.WarehouseRequest;
import com.company.wms.dto.WarehouseResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    Warehouse toEntity(WarehouseRequest request);

    WarehouseResponse toResponse(Warehouse entity);

    List<WarehouseResponse> toResponseList(List<Warehouse> entities);
}