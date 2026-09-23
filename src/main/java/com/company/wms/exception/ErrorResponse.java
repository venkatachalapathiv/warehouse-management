package com.company.wms.exception;

import org.slf4j.MDC;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
        String code,
        String message,
        String path,
        Instant timestamp,
        String correlationId,
        List<String> details
) {

    public static ErrorResponse of(String code, String message, String path, List<String> details) {
        return new ErrorResponse(
                code,
                message,
                path,
                Instant.now(),
                MDC.get("correlationId"),
                details == null ? List.of() : details);
    }
}