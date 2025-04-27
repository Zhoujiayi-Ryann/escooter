package com.example.hello.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Builder;

/**
 * 优惠券分发响应DTO
 */
@Data
@Builder
public class CouponDistributeResponse {
    @JsonProperty("success_count")
    private Integer successCount;
    
    @JsonProperty("fail_count")
    private Integer failCount;
    
    public static CouponDistributeResponse of(int successCount, int failCount) {
        return CouponDistributeResponse.builder()
                .successCount(successCount)
                .failCount(failCount)
                .build();
    }
} 