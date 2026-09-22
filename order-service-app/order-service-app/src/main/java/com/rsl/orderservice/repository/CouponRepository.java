package com.rsl.orderservice.repository;

import com.rsl.orderservice.model.Coupon;

import java.util.HashMap;
import java.util.Map;

/** In-memory store of coupons, keyed by coupon code. */
public class CouponRepository {

    private final Map<String, Coupon> coupons = new HashMap<>();

    public void save(Coupon coupon) {
        coupons.put(coupon.getCode(), coupon);
    }

    /** Returns the coupon, or {@code null} if the code is unknown. */
    public Coupon findByCode(String code) {
        return coupons.get(code);
    }
}
