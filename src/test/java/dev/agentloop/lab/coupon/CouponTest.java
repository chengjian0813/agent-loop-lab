package dev.agentloop.lab.coupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class CouponTest {

    @Test
    void fullReductionAppliesAtThreshold() {
        var coupon = new FullReductionCoupon("SAVE20", new BigDecimal("100"), new BigDecimal("20"));
        assertThat(coupon.isApplicable(Money.of("100"))).isTrue();
        assertThat(coupon.discountFor(Money.of("100"))).isEqualByComparingTo("20.00");
        assertThat(coupon.description()).isEqualTo("满 100.00 减 20.00");
    }

    @Test
    void fullReductionBelowThresholdGivesNoDiscount() {
        var coupon = new FullReductionCoupon("SAVE20", new BigDecimal("100"), new BigDecimal("20"));
        assertThat(coupon.isApplicable(Money.of("99.99"))).isFalse();
        assertThat(coupon.discountFor(Money.of("99.99"))).isEqualByComparingTo("0.00");
    }

    @Test
    void fullReductionNeverExceedsSubtotal() {
        var coupon = new FullReductionCoupon("FREE", BigDecimal.ZERO, new BigDecimal("50"));
        assertThat(coupon.discountFor(Money.of("30"))).isEqualByComparingTo("30.00");
    }

    @Test
    void fullReductionRejectsInvalidArguments() {
        assertThatThrownBy(() -> new FullReductionCoupon("X", new BigDecimal("-1"), BigDecimal.ONE))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new FullReductionCoupon("X", BigDecimal.ONE, BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void percentOffRoundsHalfUp() {
        var coupon = new PercentOffCoupon("TEN_OFF", 10, null);
        assertThat(coupon.discountFor(Money.of("33.35"))).isEqualByComparingTo("3.34");
        assertThat(coupon.description()).isEqualTo("打 9.0 折");
    }

    @Test
    void percentOffRespectsCap() {
        var coupon = new PercentOffCoupon("VIP15", 15, new BigDecimal("30"));
        assertThat(coupon.discountFor(Money.of("100"))).isEqualByComparingTo("15.00");
        assertThat(coupon.discountFor(Money.of("1000"))).isEqualByComparingTo("30.00");
        assertThat(coupon.description()).contains("最高减 30.00");
    }

    @Test
    void percentOffNotApplicableToZeroSubtotal() {
        var coupon = new PercentOffCoupon("TEN_OFF", 10, null);
        assertThat(coupon.isApplicable(Money.ZERO)).isFalse();
        assertThat(coupon.discountFor(Money.ZERO)).isEqualByComparingTo("0.00");
    }

    @Test
    void percentOffRejectsInvalidArguments() {
        assertThatThrownBy(() -> new PercentOffCoupon("X", 0, null)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new PercentOffCoupon("X", 100, null)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new PercentOffCoupon("X", 10, BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void repositoryLooksUpCaseInsensitively() {
        var repo = new CouponRepository();
        assertThat(repo.findByCode(" save20 ")).isPresent();
        assertThat(repo.findByCode("NOPE")).isEmpty();
        assertThat(repo.findByCode(null)).isEmpty();
        assertThat(repo.findAll()).hasSize(4);
    }
}
