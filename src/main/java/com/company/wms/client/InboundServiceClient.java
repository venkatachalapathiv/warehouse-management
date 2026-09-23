package com.company.wms.client;

import com.company.wms.dto.inbound.GoodsReceiptRequest;

import com.company.wms.dto.inbound.GoodsReceiptResponse;
import com.company.wms.exception.InboundServiceUnavailableException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "inboundService",
        url = "${inbound.service.url}"
)
public interface InboundServiceClient {

    @PostMapping("/api/v1/goods-receipts")
    @CircuitBreaker(name = "inboundService", fallbackMethod = "createGoodsReceiptFallback")
    GoodsReceiptResponse createGoodsReceipt(@RequestBody GoodsReceiptRequest request);

    default GoodsReceiptResponse createGoodsReceiptFallback(
            GoodsReceiptRequest request, Throwable t) {
        throw new InboundServiceUnavailableException(
                "inbound-service unavailable: " + t.getMessage(), t);
    }
}