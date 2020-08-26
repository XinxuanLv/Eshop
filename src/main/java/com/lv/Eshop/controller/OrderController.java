package com.lv.Eshop.controller;

import com.lv.Eshop.VoType.OrderVO;
import com.lv.Eshop.beans.Order;
import com.lv.Eshop.dao.OrdersDao;
import com.lv.Eshop.http.Response;
import com.lv.Eshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrdersDao ordersDao;

    @Autowired
    OrderService orderService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping
    public Response createOrder(Authentication authentication) {
        return orderService.createOrder(authentication);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @PutMapping("/{orderNum}")
    public Response payOder(@PathVariable String orderNum,  Authentication authentication) {
        return orderService.payOrder(orderNum, authentication);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @DeleteMapping("/{orderNum}")
    public Response cancelOrder(@PathVariable String orderNum,  Authentication authentication) {
        return orderService.cancelOrder(orderNum, authentication);
    }
//
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
//    @GetMapping
//    public List<OrderVO> showOrder() {
//        return orderService.list();
//    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @GetMapping
    public List<OrderVO> showOrderUser(Authentication authentication) {
        return orderService.listForUser(authentication);
    }
}
