package com.example.DroolsTC.controller;

import com.example.DroolsTC.model.Order;
import com.example.DroolsTC.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/getDiscount")
    public Order getDiscount (@RequestBody Order order) throws FileNotFoundException {
        return orderService.getDiscountForOrderV3(order);
    }
}
