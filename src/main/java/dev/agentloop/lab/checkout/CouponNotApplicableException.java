package dev.agentloop.lab.checkout;

public class CouponNotApplicableException extends RuntimeException {

    public CouponNotApplicableException(String code, String reason) {
        super("coupon " + code + " is not applicable: " + reason);
    }
}
