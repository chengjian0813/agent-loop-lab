package dev.agentloop.lab.coupon;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/** In-memory coupon store with a few fictional seed coupons. */
@Repository
public class CouponRepository {

    private final Map<String, Coupon> coupons = new LinkedHashMap<>();

    public CouponRepository() {
        save(new FullReductionCoupon("SAVE20", new BigDecimal("100"), new BigDecimal("20")));
        save(new FullReductionCoupon("BIG50", new BigDecimal("300"), new BigDecimal("50")));
        save(new PercentOffCoupon("TEN_OFF", 10, null));
        save(new PercentOffCoupon("VIP15", 15, new BigDecimal("30")));
    }

    public Optional<Coupon> findByCode(String code) {
        if (code == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(coupons.get(code.trim().toUpperCase()));
    }

    public Collection<Coupon> findAll() {
        return coupons.values();
    }

    public void save(Coupon coupon) {
        coupons.put(coupon.code().toUpperCase(), coupon);
    }
}
