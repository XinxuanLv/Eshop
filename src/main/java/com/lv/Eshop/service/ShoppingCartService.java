package com.lv.Eshop.service;

import com.lv.Eshop.VoType.CartVo;
import com.lv.Eshop.VoType.ProductVO;
import com.lv.Eshop.beans.Goods;
import com.lv.Eshop.beans.ShoppingCart;
import com.lv.Eshop.beans.SubCart;
import com.lv.Eshop.beans.User;
import com.lv.Eshop.dao.GoodsDao;
import com.lv.Eshop.dao.ShoppingCartDao;
import com.lv.Eshop.dao.SubCartDao;
import com.lv.Eshop.dao.UserDao;
import com.lv.Eshop.http.Response;
import com.lv.Eshop.returnType.OrderReturnType;
import com.lv.Eshop.security.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ShoppingCartService {
    @Autowired
    ShoppingCartDao shoppingCartDao;

    @Autowired
    SubCartDao subCartDao;

    @Autowired
    UserDao userDao;

    @Autowired
    GoodsDao goodsDao;

    @Autowired
    UserServiceImpl userService;

    public Response addToCart(OrderReturnType orderReturnType, Authentication authentication){
        String userName = authentication.getName();
        int productId = orderReturnType.getProductId();
        int num = orderReturnType.getNum();
        //1.check the item and user is legal;
        User user = userDao.findByUsername(userName);
        if (user == null){
            return new Response(false, "No such user/ wrong user type");
        }

        Goods goods = goodsDao.findById(productId);
        if (goods == null){
            return new Response(false,"No such product");
        }
        if (goods.getStock() <= 0){
            return new Response(false,"no Stock");
        }

        //2. add to cart
        // check the cart if it already in the cart;
        ShoppingCart shoppingCart = user.getShoppingCart();
        List <SubCart> list = user.getShoppingCart().getSubCarts();
        for (int i = 0; i < list.size(); i ++){
            SubCart subCart = list.get(i);
            if (subCart.getProductId() == productId) {
                if ((subCart.getProductNum() + num) <= goods.getStock()){
                    subCart.setProductNum(subCart.getProductNum() + num);
                    return new Response(true);
                }else {
                    return new Response(false,"exceed the max of stock");
                }
            }
        }
        SubCart subCart = new SubCart();
        subCart.setProductId(productId);
        subCart.setProductNum(num);
        subCart.setCart(shoppingCart);
        subCartDao.save(subCart);
        list.add(subCart);
        shoppingCart.setSubCarts(list);
        shoppingCartDao.save(shoppingCart);
        return new Response(true);
    }

    public Response changeItemNum(OrderReturnType orderReturnType, Authentication authentication){
        String userName = authentication.getName();
        int productId = orderReturnType.getProductId();
        int num = orderReturnType.getNum();
        //1.check the item and user is legal;
        if (num < 0){
            return new Response(false,"Cannot change the Item num less than 0!");
        }
        User user = userDao.findByUsername(userName);
        if (user == null){
            return new Response(false, "No such user/ wrong user type");
        }

        Goods goods = goodsDao.findById(productId);
        if (goods == null){
            return new Response(false,"No such product");
        }

        if (num > goods.getStock()){
            return new Response(false,"Cannot change the Item num exceeds the stock!");
        }

        ShoppingCart shoppingCart = user.getShoppingCart();
        List<SubCart> list = shoppingCart.getSubCarts();
        boolean check = false;
        for (int i = 0; i < list.size(); i++){
            SubCart subCart = list.get(i);
            if (subCart.getProductId() == productId){
                check = true;
                if (num == 0){
                    subCartDao.deleteByProductId(productId);
                    list.remove(subCart);
                    shoppingCart.setSubCarts(list);
                }else {
                    subCart.setProductNum(num);
                    subCartDao.save(subCart);
                }
            }
        }
        if (check){
            return new Response(true);
        }
        else {
            return new Response(false,"Did not find the item");
        }
    }

    public Response deleteCart(Authentication authentication){
        String userName = authentication.getName();
        User user = userDao.findByUsername(userName);
        if (user == null){
            return new Response(false, "No such user/ wrong user type");
        }
        ShoppingCart shoppingCart = user.getShoppingCart();
        List<SubCart> list = shoppingCart.getSubCarts();
        subCartDao.deleteByCartId(shoppingCart);
        list.clear();
        shoppingCart.setSubCarts(list);
        shoppingCartDao.save(shoppingCart);
        return new Response(true);
    }

    public CartVo listForUser(Authentication authentication){
        String userName = authentication.getName();
        User user = userDao.findByUsername(userName);
//        BigDecimal amount = new BigDecimal("0");
        List<ProductVO> productVOList = new ArrayList<ProductVO>();
        ShoppingCart shoppingCart = shoppingCartDao.findById(user.getId());
        List<SubCart> subCartList = shoppingCart.getSubCarts();
        for (int i = 0; i < subCartList.size(); i ++){
            ProductVO productVO = new ProductVO();
            Goods goods = goodsDao.findById(subCartList.get(i).getProductId());
            productVO.setId(goods.getId());
            productVO.setStock(goods.getStock());
            productVO.setProductName(goods.getName());
            productVO.setProductNum(subCartList.get(i).getProductNum());
            productVO.setProductPrice(goods.getPrice());
            productVO.setProductUrl(goods.getUrl());
            productVOList.add(productVO);
//            amount = amount.add(productVO.getProductPrice().multiply(BigDecimal.valueOf(productVO.getProductNum())));
        }
        CartVo cartVo = new CartVo();
        cartVo.setProductVOList(productVOList);
//        cartVo.setAmount(amount);
        return cartVo;
    }
}

