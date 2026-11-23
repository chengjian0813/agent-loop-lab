package dev.agentloop.lab.checkout;

public class CouponNotFoundException extends RuntimeException {

    public CouponNotFoundException(String code) {
        super("coupon not found: " + code);
    }
}
