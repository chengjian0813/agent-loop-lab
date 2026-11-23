package dev.agentloop.lab.checkout;

import dev.agentloop.lab.coupon.Coupon;
import dev.agentloop.lab.coupon.CouponRepository;
import dev.agentloop.lab.coupon.Money;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

/** Prices a cart and applies at most one coupon. */
@Service
public class CheckoutService {

    private final CouponRepository couponRepository;

    public CheckoutService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public Quote quote(List<CartItem> items, String couponCode) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("cart must not be empty");
        }
        BigDecimal subtotal = Money.normalize(items.stream()
                .map(CartItem::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        if (couponCode == null || couponCode.isBlank()) {
            return new Quote(subtotal, Money.ZERO, subtotal, null);
        }

        Coupon coupon = couponRepository.findByCode(couponCode)
                .orElseThrow(() -> new CouponNotFoundException(couponCode));
        if (!coupon.isApplicable(subtotal)) {
            throw new CouponNotApplicableException(coupon.code(), coupon.description());
        }
        BigDecimal discount = coupon.discountFor(subtotal);
        BigDecimal payable = subtotal.subtract(discount).max(Money.ZERO);
        return new Quote(subtotal, discount, payable, coupon.code());
    }
}
