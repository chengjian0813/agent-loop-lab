package dev.agentloop.lab.checkout;

import java.math.BigDecimal;

/** Result of pricing a cart. {@code appliedCoupon} is null when no coupon was applied. */
public record Quote(BigDecimal subtotal, BigDecimal discount, BigDecimal payable, String appliedCoupon) {
}
