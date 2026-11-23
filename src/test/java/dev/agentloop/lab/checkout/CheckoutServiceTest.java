package dev.agentloop.lab.checkout;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.agentloop.lab.coupon.CouponRepository;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class CheckoutServiceTest {

    private final CheckoutService service = new CheckoutService(new CouponRepository());

    private static CartItem item(String sku, String price, int qty) {
        return new CartItem(sku, new BigDecimal(price), qty);
    }

    @Test
    void quoteWithoutCoupon() {
        Quote quote = service.quote(List.of(item("BOOK-1", "39.90", 2), item("PEN-1", "5.00", 3)), null);
        assertThat(quote.subtotal()).isEqualByComparingTo("94.80");
        assertThat(quote.discount()).isEqualByComparingTo("0.00");
        assertThat(quote.payable()).isEqualByComparingTo("94.80");
        assertThat(quote.appliedCoupon()).isNull();
    }

    @Test
    void blankCouponIsIgnored() {
        Quote quote = service.quote(List.of(item("BOOK-1", "10.00", 1)), "  ");
        assertThat(quote.appliedCoupon()).isNull();
    }

    @Test
    void quoteWithFullReductionCoupon() {
        Quote quote = service.quote(List.of(item("BOOK-1", "60.00", 2)), "save20");
        assertThat(quote.discount()).isEqualByComparingTo("20.00");
        assertThat(quote.payable()).isEqualByComparingTo("100.00");
        assertThat(quote.appliedCoupon()).isEqualTo("SAVE20");
    }

    @Test
    void quoteWithCappedPercentCoupon() {
        Quote quote = service.quote(List.of(item("LAMP-1", "500.00", 1)), "VIP15");
        assertThat(quote.discount()).isEqualByComparingTo("30.00");
        assertThat(quote.payable()).isEqualByComparingTo("470.00");
    }

    @Test
    void unknownCouponIsRejected() {
        assertThatThrownBy(() -> service.quote(List.of(item("BOOK-1", "10.00", 1)), "NOPE"))
                .isInstanceOf(CouponNotFoundException.class);
    }

    @Test
    void couponBelowThresholdIsRejected() {
        assertThatThrownBy(() -> service.quote(List.of(item("BOOK-1", "10.00", 1)), "SAVE20"))
                .isInstanceOf(CouponNotApplicableException.class)
                .hasMessageContaining("满 100.00 减 20.00");
    }

    @Test
    void emptyCartIsRejected() {
        assertThatThrownBy(() -> service.quote(List.of(), null)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> service.quote(null, null)).isInstanceOf(IllegalArgumentException.class);
    }
}
