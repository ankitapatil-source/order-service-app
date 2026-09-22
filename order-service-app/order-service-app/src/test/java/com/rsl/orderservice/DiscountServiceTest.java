package com.rsl.orderservice;

import com.rsl.orderservice.model.Coupon;
import com.rsl.orderservice.model.Customer;
import com.rsl.orderservice.repository.CouponRepository;
import com.rsl.orderservice.service.DiscountService;
import com.rsl.orderservice.service.PricingService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DiscountServiceTest {

    private DiscountService newDiscountService() {
        CouponRepository coupons = new CouponRepository();
        coupons.save(new Coupon("SAVE10", 10));
        return new DiscountService(coupons, new PricingService());
    }

    @Test
    void noCouponAndNonMemberMeansNoDiscount() {
        DiscountService discounts = newDiscountService();
        Customer bob = new Customer("C-2", "Bob", false);

        assertEquals(0, discounts.discountCents(1000, bob, null));
    }

    @Test
    void unknownCouponCodeIsIgnoredNotFatal() {
        DiscountService discounts = newDiscountService();
        Customer bob = new Customer("C-2", "Bob", false);

        // A coupon code the customer mistyped must not bring the order down.
        assertDoesNotThrow(() -> discounts.discountCents(1000, bob, "BLACKFRIDAY"));
    }
}
