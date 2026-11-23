package dev.agentloop.lab.coupon;

import java.math.BigDecimal;

/**
 * A coupon that can reduce the subtotal of a cart.
 * Implementations must never return a discount larger than the subtotal.
 */
public sealed interface Coupon permits FullReductionCoupon, PercentOffCoupon {

    String code();

    String description();

    /** Whether this coupon can be used for the given subtotal. */
    boolean isApplicable(BigDecimal subtotal);

    /** Discount for the given subtotal, scale 2. Returns zero when not applicable. */
    BigDecimal discountFor(BigDecimal subtotal);
}
