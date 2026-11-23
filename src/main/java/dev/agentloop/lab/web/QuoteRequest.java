package dev.agentloop.lab.web;

import dev.agentloop.lab.checkout.CartItem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record QuoteRequest(@NotEmpty List<@Valid CartItem> items, String couponCode) {
}
