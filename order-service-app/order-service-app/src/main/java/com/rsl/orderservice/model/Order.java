package com.rsl.orderservice.model;

import java.util.ArrayList;
import java.util.List;

/** An order placed by a customer. Totals are filled in by the pricing step. */
public class Order {

    private final String id;
    private final Customer customer;
    private final List<OrderItem> items = new ArrayList<>();

    private OrderStatus status = OrderStatus.NEW;
    private int subtotalCents;
    private int discountCents;
    private int totalCents;
    private String couponCode;

    public Order(String id, Customer customer) {
        this.id = id;
        this.customer = customer;
    }

    public String getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public int getSubtotalCents() {
        return subtotalCents;
    }

    public void setSubtotalCents(int subtotalCents) {
        this.subtotalCents = subtotalCents;
    }

    public int getDiscountCents() {
        return discountCents;
    }

    public void setDiscountCents(int discountCents) {
        this.discountCents = discountCents;
    }

    public int getTotalCents() {
        return totalCents;
    }

    public void setTotalCents(int totalCents) {
        this.totalCents = totalCents;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    @Override
    public String toString() {
        return "Order{" + id + ", " + customer.getName() + ", " + status
                + ", subtotal=" + subtotalCents + "c, discount=" + discountCents
                + "c, total=" + totalCents + "c}";
    }
}
