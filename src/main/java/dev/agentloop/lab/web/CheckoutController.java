package dev.agentloop.lab.web;

import dev.agentloop.lab.checkout.CheckoutService;
import dev.agentloop.lab.checkout.Quote;
import dev.agentloop.lab.coupon.CouponRepository;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final CouponRepository couponRepository;

    public CheckoutController(CheckoutService checkoutService, CouponRepository couponRepository) {
        this.checkoutService = checkoutService;
        this.couponRepository = couponRepository;
    }

    @GetMapping("/coupons")
    public List<CouponView> coupons() {
        return couponRepository.findAll().stream()
                .map(c -> new CouponView(c.code(), c.description()))
                .toList();
    }

    @PostMapping("/checkout/quote")
    public Quote quote(@Valid @RequestBody QuoteRequest request) {
        return checkoutService.quote(request.items(), request.couponCode());
    }
}
