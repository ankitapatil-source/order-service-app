package com.rsl.orderservice.repository;

import com.rsl.orderservice.model.Order;

import java.util.HashMap;
import java.util.Map;

/** In-memory store of orders, keyed by order id. */
public class OrderRepository {

    private final Map<String, Order> orders = new HashMap<>();

    public void save(Order order) {
        orders.put(order.getId(), order);
    }

    /** Returns the order, or {@code null} if no order has that id. */
    public Order findById(String orderId) {
        return orders.get(orderId);
    }

    public int count() {
        return orders.size();
    }
}
