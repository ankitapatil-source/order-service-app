package com.rsl.orderservice.repository;

import com.rsl.orderservice.model.Product;

import java.util.HashMap;
import java.util.Map;

/** In-memory store of products, keyed by SKU. */
public class ProductRepository {

    private final Map<String, Product> products = new HashMap<>();

    public void save(Product product) {
        products.put(product.getSku(), product);
    }

    public Product findBySku(String sku) {
        return products.get(sku);
    }

    public boolean exists(String sku) {
        return products.containsKey(sku);
    }
}
