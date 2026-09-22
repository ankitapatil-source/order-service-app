package com.rsl.orderservice.service;

import com.rsl.orderservice.util.AppLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Tracks how many units of each product are in stock and reserves stock
 * when an order is placed.
 */
public class InventoryService {

    private static final Logger log = AppLogger.get(InventoryService.class);

    private final Map<String, Integer> stockBySku = new HashMap<>();

    /** Set the stock level for a SKU. */
    public void setStock(String sku, int units) {
        stockBySku.put(sku, units);
    }

    /** How many units are currently in stock for a SKU. */
    public int available(String sku) {
        return stockBySku.getOrDefault(sku, 0);
    }

    /**
     * Reserve {@code quantity} units of {@code sku}. A reservation should only
     * succeed when there is enough stock; otherwise stock is left untouched.
     *
     * @return true if the units were reserved, false if there was not enough stock
     */
    public boolean reserve(String sku, int quantity) {
        int inStock = available(sku);
        if (inStock < quantity) {
            log.warning("Refused to reserve " + quantity + " of " + sku
                    + " (only " + inStock + " in stock)");
            return false;
        }
        stockBySku.put(sku, inStock - quantity);
        log.info("Reserved " + quantity + " of " + sku + " (was " + inStock
                + ", now " + available(sku) + ")");
        return true;
    }
}
