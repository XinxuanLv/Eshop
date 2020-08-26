package com.lv.Eshop.service;

import com.lv.Eshop.beans.Goods;
import com.lv.Eshop.dao.GoodsDao;
import com.lv.Eshop.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GoodsService {
    @Autowired
    GoodsDao goodsDao;

    public Response addGoods(Goods goods, Authentication authentication) {
        System.out.println(goods);
        String saleName = authentication.getName();
        goods.setSaleName(saleName);
        Goods duplicatedGoods = goodsDao.findByName(goods.getName());
        if (duplicatedGoods != null){
            return new Response(false,"The product is already added");
        }
        goodsDao.save(goods);
        return new Response(true);
    }

    public Response changeGoods(Goods goods, Authentication authentication) {
        Goods g = goodsDao.findByName(goods.getName());
        String saleName = authentication.getName();
        if (g != null) {
            if (goods.getSaleName() != saleName){
                return new Response(false,"Don't have authority to change other's product");
            }
            g.setName(goods.getName());
            g.setDescription(goods.getDescription());
            g.setPrice(goods.getPrice());
            g.setStock(goods.getStock());
            g.setUrl(goods.getUrl());
            goodsDao.save(g);
        } else {
            return new Response(false, "The product did not find");
        }
        return new Response(true);
    }

    public Response deleteGoods(int id, Authentication authentication) {
        Goods goods =goodsDao.findById(id);
        if (goods != null) {
            String saleName = authentication.getName();
            if (!goods.getSaleName().equals(saleName)){
                return new Response(false,"Don't have authority to change other's product");
            }
            goodsDao.delete(id);
            return new Response(true);
        } else {
            return new Response(false);
        }
    }
}
