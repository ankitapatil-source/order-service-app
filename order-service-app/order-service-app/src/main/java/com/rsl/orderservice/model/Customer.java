package com.rsl.orderservice.model;

/** A customer who can place orders. */
public class Customer {

    private final String id;
    private final String name;
    private final boolean member;

    public Customer(String id, String name, boolean member) {
        this.id = id;
        this.name = name;
        this.member = member;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /** Members are eligible for the loyalty discount. */
    public boolean isMember() {
        return member;
    }

    @Override
    public String toString() {
        return "Customer{" + id + ", " + name + (member ? ", member" : "") + "}";
    }
}
