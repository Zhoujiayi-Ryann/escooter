package com.example.hello.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

/**
 * 优惠券分发请求DTO
 */
@Data
public class CouponDistributeRequest {
    @JsonProperty("coupon_id")
    private Integer couponId;
    
    @JsonProperty("user_ids")
    private List<Integer> userIds;
} 