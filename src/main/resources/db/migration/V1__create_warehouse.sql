CREATE TABLE warehouse (
    id              UUID PRIMARY KEY,
    warehouse_code  VARCHAR(50)  NOT NULL,
    name            VARCHAR(150) NOT NULL,
    address         VARCHAR(500),
    status          VARCHAR(20)  NOT NULL,
    created_at      TIMESTAMPTZ  NOT NULL,
    updated_at      TIMESTAMPTZ  NOT NULL,
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    version         BIGINT       NOT NULL DEFAULT 0,
    CONSTRAINT uk_warehouse_code UNIQUE (warehouse_code),
    CONSTRAINT chk_warehouse_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED'))
);

CREATE INDEX idx_warehouse_status ON warehouse(status);
CREATE INDEX idx_warehouse_name   ON warehouse(name);