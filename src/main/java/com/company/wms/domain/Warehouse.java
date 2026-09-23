package com.company.wms.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "warehouse", uniqueConstraints = @UniqueConstraint(name = "uk_warehouse_code", columnNames = "warehouse_code"))
public class Warehouse extends AuditableEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "warehouse_code", nullable = false, length = 50)
	private String warehouseCode;

	@Column(nullable = false, length = 150)
	private String name;

	@Column(length = 500)
	private String address;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private WarehouseStatus status;

	@Version
	private Long version;

	public Warehouse() {
	}

	public Warehouse(UUID id, String warehouseCode, String name, String address, WarehouseStatus status, Long version) {
		this.id = id;
		this.warehouseCode = warehouseCode;
		this.name = name;
		this.address = address;
		this.status = status;
		this.version = version;
	}

	public UUID getId() {
		return id;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public WarehouseStatus getStatus() {
		return status;
	}

	public Long getVersion() {
		return version;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setStatus(WarehouseStatus status) {
		this.status = status;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
}