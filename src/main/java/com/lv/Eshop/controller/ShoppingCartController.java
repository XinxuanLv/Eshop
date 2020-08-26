package com.lv.Eshop.controller;

import com.lv.Eshop.VoType.CartVo;
import com.lv.Eshop.beans.SubCart;
import com.lv.Eshop.dao.ShoppingCartDao;
import com.lv.Eshop.dao.SubCartDao;
import com.lv.Eshop.http.Response;
import com.lv.Eshop.returnType.OrderReturnType;
import com.lv.Eshop.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    ShoppingCartDao shoppingCartDao;

    @Autowired
    SubCartDao subCartDao;

    @Autowired
    ShoppingCartService shoppingCartService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping
    public Response addToCart(@RequestBody OrderReturnType orderReturnType, Authentication authentication) {
        return shoppingCartService.addToCart(orderReturnType, authentication);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @PutMapping
    public Response changeNumOfItem(@RequestBody OrderReturnType orderReturnType, Authentication authentication){
        return shoppingCartService.changeItemNum(orderReturnType, authentication);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @DeleteMapping
    public Response deleteCart(Authentication authentication){
        return shoppingCartService.deleteCart(authentication);
    }

    @GetMapping
    public CartVo getCart(Authentication authentication) {
        return shoppingCartService.listForUser(authentication);
    }
}
