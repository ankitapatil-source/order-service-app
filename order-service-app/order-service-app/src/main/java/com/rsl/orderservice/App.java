package com.rsl.orderservice;

import com.rsl.orderservice.model.Coupon;
import com.rsl.orderservice.model.Customer;
import com.rsl.orderservice.model.Order;
import com.rsl.orderservice.model.Product;
import com.rsl.orderservice.repository.CouponRepository;
import com.rsl.orderservice.repository.OrderRepository;
import com.rsl.orderservice.repository.ProductRepository;
import com.rsl.orderservice.service.DiscountService;
import com.rsl.orderservice.service.InventoryService;
import com.rsl.orderservice.service.OrderService;
import com.rsl.orderservice.service.PricingService;
import com.rsl.orderservice.util.AppLogger;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Runs a few order scenarios end to end so the application produces real
 * console output and log lines in <code>logs/app.log</code>.
 */
public class App {

    private static final Logger log = AppLogger.get(App.class);

    public static void main(String[] args) {
        log.info("=== Order Service starting ===");

        // ---- Wire the application together ----
        ProductRepository products = new ProductRepository();
        OrderRepository orders = new OrderRepository();
        CouponRepository coupons = new CouponRepository();

        InventoryService inventory = new InventoryService();
        PricingService pricing = new PricingService();
        DiscountService discounts = new DiscountService(coupons, pricing);
        OrderService orderService =
                new OrderService(products, orders, inventory, pricing, discounts);

        // ---- Seed reference data ----
        products.save(new Product("BOOK-001", "Clean Code", 3200, "books"));
        products.save(new Product("MUG-010", "Coffee Mug", 900, "kitchen"));
        inventory.setStock("BOOK-001", 5);
        inventory.setStock("MUG-010", 3);
        coupons.save(new Coupon("SAVE10", 10));

        Customer alice = new Customer("C-1", "Alice", true);   // member
        Customer bob = new Customer("C-2", "Bob", false);      // non-member

        // ---- Scenario A: a normal order with a valid coupon ----
        runScenario("A: valid order with coupon SAVE10", () -> {
            Order order = orderService.placeOrder(
                    "ORD-A", alice,
                    List.<String[]>of(new String[]{"BOOK-001", "2"}),
                    "SAVE10");
            System.out.println("  -> " + order);
        });

        // ---- Scenario B: an order with a coupon code that does not exist ----
        runScenario("B: order with unknown coupon BLACKFRIDAY", () -> {
            Order order = orderService.placeOrder(
                    "ORD-B", bob,
                    List.<String[]>of(new String[]{"MUG-010", "1"}),
                    "BLACKFRIDAY");
            System.out.println("  -> " + order);
        });

        log.info("=== Order Service finished ===");
    }

    /** Runs one scenario, logging (with full stack trace) anything it throws. */
    private static void runScenario(String title, Runnable scenario) {
        System.out.println();
        System.out.println("Scenario " + title);
        log.info("--- Scenario " + title + " ---");
        try {
            scenario.run();
        } catch (RuntimeException e) {
            System.out.println("  -> FAILED: " + e);
            log.log(Level.SEVERE, "Scenario failed: " + title, e);
        }
    }
}
