package com.rsl.orderservice;

import com.rsl.orderservice.model.Customer;
import com.rsl.orderservice.model.Order;
import com.rsl.orderservice.model.OrderItem;
import com.rsl.orderservice.model.Product;
import com.rsl.orderservice.service.PricingService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PricingServiceTest {

    private final PricingService pricing = new PricingService();

    @Test
    void subtotalAddsUpEveryLine() {
        Product book = new Product("BOOK-001", "Clean Code", 3200, "books");
        Product mug = new Product("MUG-010", "Coffee Mug", 900, "kitchen");

        Order order = new Order("ORD-1", new Customer("C-1", "Alice", true));
        order.addItem(new OrderItem(book, 2));   // 6400c
        order.addItem(new OrderItem(mug, 3));    // 2700c

        assertEquals(9100, pricing.subtotalCents(order));
    }

    @Test
    void tenPercentOfTwentyDollarsIsTwoDollars() {
        // 10% of 2000c should be 200c.
        assertEquals(200, pricing.percentageDiscountCents(2000, 10));
    }

    @Test
    void twentyFivePercentOfFortyDollarsIsTenDollars() {
        // 25% of 4000c should be 1000c.
        assertEquals(1000, pricing.percentageDiscountCents(4000, 25));
    }

    @Test
    void zeroPercentIsNoDiscount() {
        assertEquals(0, pricing.percentageDiscountCents(5000, 0));
    }
}
