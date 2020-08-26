package com.lv.Eshop.dao;

import com.lv.Eshop.beans.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartDao extends JpaRepository<ShoppingCart, Integer> {
    ShoppingCart findById(int id);
}
