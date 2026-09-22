package com.rsl.orderservice.model;

/**
 * A product that can be sold. Prices are stored in whole cents to avoid
 * floating point money errors (e.g. 1999 == $19.99).
 */
public class Product {

    private final String sku;
    private final String name;
    private final int unitPriceCents;
    private final String category;

    public Product(String sku, String name, int unitPriceCents, String category) {
        this.sku = sku;
        this.name = name;
        this.unitPriceCents = unitPriceCents;
        this.category = category;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public int getUnitPriceCents() {
        return unitPriceCents;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Product{" + sku + ", " + name + ", " + unitPriceCents + "c}";
    }
}
