package dev.agentloop.lab.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CheckoutControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void listsCoupons() throws Exception {
        mvc.perform(get("/api/coupons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("SAVE20"))
                .andExpect(jsonPath("$.length()").value(4));
    }

    @Test
    void quotesCartWithCoupon() throws Exception {
        String body = """
                {"items":[{"sku":"BOOK-1","unitPrice":60.00,"quantity":2}],"couponCode":"SAVE20"}
                """;
        mvc.perform(post("/api/checkout/quote").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subtotal").value(120.00))
                .andExpect(jsonPath("$.discount").value(20.00))
                .andExpect(jsonPath("$.payable").value(100.00))
                .andExpect(jsonPath("$.appliedCoupon").value("SAVE20"));
    }

    @Test
    void unknownCouponReturns404() throws Exception {
        String body = """
                {"items":[{"sku":"BOOK-1","unitPrice":10,"quantity":1}],"couponCode":"NOPE"}
                """;
        mvc.perform(post("/api/checkout/quote").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void notApplicableCouponReturns422() throws Exception {
        String body = """
                {"items":[{"sku":"BOOK-1","unitPrice":10,"quantity":1}],"couponCode":"SAVE20"}
                """;
        mvc.perform(post("/api/checkout/quote").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void invalidItemReturns400() throws Exception {
        String body = """
                {"items":[{"sku":"","unitPrice":10,"quantity":0}]}
                """;
        mvc.perform(post("/api/checkout/quote").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }
}
