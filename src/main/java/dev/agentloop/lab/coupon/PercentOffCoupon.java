package dev.agentloop.lab.coupon;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Percentage discount with an optional cap, e.g. 9 折 (percentOff = 10), 最高减 30.
 * A null cap means "no cap".
 */
public record PercentOffCoupon(String code, int percentOff, BigDecimal cap) implements Coupon {

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    public PercentOffCoupon {
        Objects.requireNonNull(code, "code");
        if (percentOff <= 0 || percentOff >= 100) {
            throw new IllegalArgumentException("percentOff must be between 1 and 99");
        }
        if (cap != null) {
            if (cap.signum() <= 0) {
                throw new IllegalArgumentException("cap must be > 0");
            }
            cap = Money.normalize(cap);
        }
    }

    @Override
    public String description() {
        String base = "打 " + (100 - percentOff) / 10.0 + " 折";
        return cap == null ? base : base + "，最高减 " + cap.toPlainString();
    }

    @Override
    public boolean isApplicable(BigDecimal subtotal) {
        return subtotal.signum() > 0;
    }

    @Override
    public BigDecimal discountFor(BigDecimal subtotal) {
        if (!isApplicable(subtotal)) {
            return Money.ZERO;
        }
        BigDecimal raw = Money.normalize(subtotal.multiply(BigDecimal.valueOf(percentOff)).divide(HUNDRED));
        return cap == null ? raw : raw.min(cap);
    }
}
