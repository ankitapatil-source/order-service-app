package com.rsl.orderservice.model;

/**
 * A percentage-off coupon, e.g. code "SAVE10" for 10% off.
 */
public class Coupon {

    private final String code;
    private final int percentOff;

    public Coupon(String code, int percentOff) {
        this.code = code;
        this.percentOff = percentOff;
    }

    public String getCode() {
        return code;
    }

    /** Whole percent, e.g. 10 means 10% off. */
    public int getPercentOff() {
        return percentOff;
    }

    @Override
    public String toString() {
        return "Coupon{" + code + ", " + percentOff + "%}";
    }
}
