package dev.agentloop.lab.coupon;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Money helpers. All amounts in this lab use scale 2 and HALF_UP rounding. */
public final class Money {

    public static final BigDecimal ZERO = BigDecimal.ZERO.setScale(2);

    private Money() {
    }

    public static BigDecimal of(String amount) {
        return normalize(new BigDecimal(amount));
    }

    public static BigDecimal normalize(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }
}
