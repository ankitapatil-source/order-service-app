package com.rsl.orderservice.service;

import com.rsl.orderservice.model.Order;
import com.rsl.orderservice.model.OrderItem;

/**
 * Works out how much an order costs.
 *
 * <p>Responsibilities:</p>
 * <ul>
 *   <li>Add up every line into a subtotal.</li>
 *   <li>Turn a whole-percent discount into a cash amount.</li>
 * </ul>
 */
public class PricingService {

    /** Sum of every line total, in cents. */
    public int subtotalCents(Order order) {
        int subtotal = 0;
        for (OrderItem item : order.getItems()) {
            subtotal += item.lineTotalCents();
        }
        return subtotal;
    }

    /**
     * Cash value, in cents, of a whole-percent discount applied to an amount.
     * For example, 10% of 2000c should be 200c.
     *
     * @param amountCents the amount the discount applies to, in cents
     * @param percent     the whole-percent discount, e.g. 10 for 10%
     * @return the discount amount, in cents
     */
    public int percentageDiscountCents(int amountCents, int percent) {
        return amountCents * percent / 100;
    }
}
