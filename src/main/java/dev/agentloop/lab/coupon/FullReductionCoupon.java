package dev.agentloop.lab.coupon;

import java.math.BigDecimal;
import java.util.Objects;

/** "Spend {threshold}, save {amount}", e.g. 满 100 减 20. */
public record FullReductionCoupon(String code, BigDecimal threshold, BigDecimal amount) implements Coupon {

    public FullReductionCoupon {
        Objects.requireNonNull(code, "code");
        if (threshold.signum() < 0 || amount.signum() <= 0) {
            throw new IllegalArgumentException("threshold must be >= 0 and amount must be > 0");
        }
        threshold = Money.normalize(threshold);
        amount = Money.normalize(amount);
    }

    @Override
    public String description() {
        return "满 " + threshold.toPlainString() + " 减 " + amount.toPlainString();
    }

    @Override
    public boolean isApplicable(BigDecimal subtotal) {
        return subtotal.compareTo(threshold) >= 0;
    }

    @Override
    public BigDecimal discountFor(BigDecimal subtotal) {
        if (!isApplicable(subtotal)) {
            return Money.ZERO;
        }
        return Money.normalize(amount.min(subtotal));
    }
}
