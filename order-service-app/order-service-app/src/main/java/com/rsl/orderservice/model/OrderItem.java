package com.rsl.orderservice.model;

/** A single line on an order: a product and how many of it. */
public class OrderItem {

    private final Product product;
    private final int quantity;

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    /** Price for this line before any discount, in cents. */
    public int lineTotalCents() {
        return product.getUnitPriceCents() * quantity;
    }

    @Override
    public String toString() {
        return "OrderItem{" + product.getSku() + " x" + quantity + "}";
    }
}
