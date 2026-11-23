package dev.agentloop.lab.web;

import dev.agentloop.lab.checkout.CouponNotApplicableException;
import dev.agentloop.lab.checkout.CouponNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(CouponNotFoundException.class)
    public ProblemDetail couponNotFound(CouponNotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler({CouponNotApplicableException.class, IllegalArgumentException.class})
    public ProblemDetail unprocessable(RuntimeException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
    }
}
