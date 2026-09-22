package com.rsl.orderservice;

import com.rsl.orderservice.model.Coupon;
import com.rsl.orderservice.model.Customer;
import com.rsl.orderservice.model.Order;
import com.rsl.orderservice.model.OrderStatus;
import com.rsl.orderservice.model.Product;
import com.rsl.orderservice.repository.CouponRepository;
import com.rsl.orderservice.repository.OrderRepository;
import com.rsl.orderservice.repository.ProductRepository;
import com.rsl.orderservice.service.DiscountService;
import com.rsl.orderservice.service.InventoryService;
import com.rsl.orderservice.service.OrderService;
import com.rsl.orderservice.service.PricingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderServiceTest {

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        ProductRepository products = new ProductRepository();
        OrderRepository orders = new OrderRepository();
        CouponRepository coupons = new CouponRepository();

        products.save(new Product("BOOK-001", "Clean Code", 3200, "books"));
        InventoryService inventory = new InventoryService();
        inventory.setStock("BOOK-001", 10);

        PricingService pricing = new PricingService();
        DiscountService discounts = new DiscountService(coupons, pricing);
        orderService = new OrderService(products, orders, inventory, pricing, discounts);
    }

    @Test
    void placeOrderConfirmsAndPricesTheOrder() {
        Customer bob = new Customer("C-2", "Bob", false);

        Order order = orderService.placeOrder(
                "ORD-1", bob,
                List.<String[]>of(new String[]{"BOOK-001", "2"}),
                null);

        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
        assertEquals(6400, order.getSubtotalCents());
        assertTrue(orderService.getOrder("ORD-1") != null, "order should be saved");
    }

    @Test
    void cancellingAnExistingOrderSucceeds() {
        Customer bob = new Customer("C-2", "Bob", false);
        orderService.placeOrder("ORD-1", bob,
                List.<String[]>of(new String[]{"BOOK-001", "1"}), null);

        assertTrue(orderService.cancelOrder("ORD-1"));
        assertEquals(OrderStatus.CANCELLED, orderService.getOrder("ORD-1").getStatus());
    }

    @Test
    void cancellingAMissingOrderReportsFailure() {
        // There is no such order, so cancel must report that nothing was cancelled.
        assertFalse(orderService.cancelOrder("ORD-DOES-NOT-EXIST"));
    }
}
