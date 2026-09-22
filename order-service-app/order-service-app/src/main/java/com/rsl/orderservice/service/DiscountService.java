package com.rsl.orderservice.service;

import com.rsl.orderservice.model.Coupon;
import com.rsl.orderservice.model.Customer;
import com.rsl.orderservice.repository.CouponRepository;
import com.rsl.orderservice.util.AppLogger;

import java.util.logging.Logger;

/**
 * Decides the discount percentage that applies to an order, based on
 * membership status and an optional coupon code.
 */
public class DiscountService {

    private static final Logger log = AppLogger.get(DiscountService.class);

    /** Loyalty discount for members, in whole percent. */
    private static final int MEMBER_PERCENT = 5;

    private final CouponRepository couponRepository;
    private final PricingService pricingService;

    public DiscountService(CouponRepository couponRepository, PricingService pricingService) {
        this.couponRepository = couponRepository;
        this.pricingService = pricingService;
    }

    /**
     * Work out the total discount, in cents, for an order subtotal.
     *
     * <p>Rules:</p>
     * <ul>
     *   <li>Members get {@value #MEMBER_PERCENT}% off.</li>
     *   <li>A valid coupon code adds its own percentage on top.</li>
     * </ul>
     *
     * @param subtotalCents the order subtotal, in cents
     * @param customer      the customer placing the order
     * @param couponCode    the coupon code entered, or null if none
     * @return the discount to subtract, in cents
     */
    public int discountCents(int subtotalCents, Customer customer, String couponCode) {
        int percent = 0;

        if (customer.isMember()) {
            percent += MEMBER_PERCENT;
        }

        if (couponCode != null && !couponCode.isBlank()) {
            Coupon coupon = couponRepository.findByCode(couponCode);
            log.info("Applying coupon '" + couponCode + "' -> " + coupon.getPercentOff() + "%");
            percent += coupon.getPercentOff();
        }

        return pricingService.percentageDiscountCents(subtotalCents, percent);
    }
}
