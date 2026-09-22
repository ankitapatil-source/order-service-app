package com.rsl.orderservice.service;

import com.rsl.orderservice.model.Customer;
import com.rsl.orderservice.model.Order;
import com.rsl.orderservice.model.OrderItem;
import com.rsl.orderservice.model.OrderStatus;
import com.rsl.orderservice.model.Product;
import com.rsl.orderservice.repository.OrderRepository;
import com.rsl.orderservice.repository.ProductRepository;
import com.rsl.orderservice.util.AppLogger;

import java.util.List;
import java.util.logging.Logger;

/**
 * Top-level entry point for working with orders. It coordinates the other
 * services: it reserves stock, prices the order, applies discounts, and
 * saves the result.
 *
 * <p>Execution flow for {@link #placeOrder}:</p>
 * <pre>
 *   placeOrder
 *     -> build the Order from the requested lines
 *     -> InventoryService.reserve   (hold the stock)
 *     -> PricingService.subtotalCents
 *     -> DiscountService.discountCents
 *     -> OrderRepository.save
 * </pre>
 */
public class OrderService {

    private static final Logger log = AppLogger.get(OrderService.class);

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;
    private final PricingService pricingService;
    private final DiscountService discountService;

    public OrderService(ProductRepository productRepository,
                        OrderRepository orderRepository,
                        InventoryService inventoryService,
                        PricingService pricingService,
                        DiscountService discountService) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.inventoryService = inventoryService;
        this.pricingService = pricingService;
        this.discountService = discountService;
    }

    /**
     * Place an order for a customer.
     *
     * @param orderId    id to give the new order
     * @param customer   the customer placing the order
     * @param lines      requested lines as [sku, quantity] pairs
     * @param couponCode optional coupon code, or null
     * @return the saved, priced order
     */
    public Order placeOrder(String orderId, Customer customer,
                            List<String[]> lines, String couponCode) {
        log.info("Placing order " + orderId + " for " + customer.getName());
        Order order = new Order(orderId, customer);
        order.setCouponCode(couponCode);

        for (String[] line : lines) {
            String sku = line[0];
            int qty = Integer.parseInt(line[1]);
            Product product = productRepository.findBySku(sku);
            if (!inventoryService.reserve(sku, qty)) {
                throw new IllegalStateException("Insufficient stock for " + sku);
            }
            order.addItem(new OrderItem(product, qty));
        }

        int subtotal = pricingService.subtotalCents(order);
        int discount = discountService.discountCents(subtotal, customer, couponCode);

        order.setSubtotalCents(subtotal);
        order.setDiscountCents(discount);
        order.setTotalCents(subtotal - discount);
        order.setStatus(OrderStatus.CONFIRMED);

        orderRepository.save(order);
        log.info("Confirmed " + order);
        return order;
    }

    /** Look up an order by id, or null if it does not exist. */
    public Order getOrder(String orderId) {
        return orderRepository.findById(orderId);
    }

    /**
     * Cancel an existing order.
     *
     * @param orderId the id of the order to cancel
     * @return true if an order was cancelled, false if no such order exists
     */
    public boolean cancelOrder(String orderId) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            log.warning("Cannot cancel unknown order " + orderId);
            return false;
        }
        order.setStatus(OrderStatus.CANCELLED);
        log.info("Cancelled order " + orderId);
        return true;
    }
}
