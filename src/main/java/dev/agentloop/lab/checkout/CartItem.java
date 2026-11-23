package dev.agentloop.lab.checkout;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CartItem(
        @NotBlank String sku,
        @NotNull @DecimalMin(value = "0.00") BigDecimal unitPrice,
        @Min(1) @Max(999) int quantity) {

    public BigDecimal lineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
