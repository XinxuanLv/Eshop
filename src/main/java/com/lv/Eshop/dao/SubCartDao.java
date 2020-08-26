package com.lv.Eshop.dao;

import com.lv.Eshop.beans.ShoppingCart;
import com.lv.Eshop.beans.SubCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCartDao extends JpaRepository<SubCart, Integer> {
    SubCart findById(int id);
    void deleteByCartId(ShoppingCart shoppingCart);
    void deleteByProductId(int productId);

    List<SubCart> findByCartId(int id);
}
