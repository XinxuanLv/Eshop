package com.lv.Eshop.controller;

import com.lv.Eshop.beans.Goods;
import com.lv.Eshop.dao.GoodsDao;
import com.lv.Eshop.http.Response;
import com.lv.Eshop.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    GoodsDao goodsDao;

    @Autowired
    GoodsService goodsService;

    @GetMapping
    public List<Goods> getGoods() {
        return goodsDao.findAll();
    }

    @GetMapping("/{id}")
    public Goods getOneGood(@PathVariable int id){
        return goodsDao.findById(id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_SALE')")
    @PostMapping
    public Response addGoods(@RequestBody Goods goods, Authentication authentication) {
        return goodsService.addGoods(goods, authentication);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_SALE')")
    @PutMapping
    public Response changeGoods(@RequestBody Goods goods, Authentication authentication) {
        return goodsService.changeGoods(goods, authentication);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMN', 'ROLE_SALE')")
    @DeleteMapping("/{id}")
    public Response deleteGoods(@PathVariable int id,  Authentication authentication) {
        return goodsService.deleteGoods(id, authentication);
    }
}
