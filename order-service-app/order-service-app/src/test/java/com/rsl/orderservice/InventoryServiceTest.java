package com.rsl.orderservice;

import com.rsl.orderservice.service.InventoryService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InventoryServiceTest {

    @Test
    void reserveWithinStockSucceeds() {
        InventoryService inventory = new InventoryService();
        inventory.setStock("MUG-010", 5);

        assertTrue(inventory.reserve("MUG-010", 3));
        assertEquals(2, inventory.available("MUG-010"));
    }

    @Test
    void reserveMoreThanStockFails() {
        InventoryService inventory = new InventoryService();
        inventory.setStock("MUG-010", 3);

        // Only 3 in stock, asking for 5: the reservation should be refused.
        assertFalse(inventory.reserve("MUG-010", 5));
    }

    @Test
    void stockNeverGoesNegative() {
        InventoryService inventory = new InventoryService();
        inventory.setStock("MUG-010", 3);

        inventory.reserve("MUG-010", 5);

        // Whatever the reservation decides, we must never oversell.
        assertTrue(inventory.available("MUG-010") >= 0,
                "stock went negative: " + inventory.available("MUG-010"));
    }
}
