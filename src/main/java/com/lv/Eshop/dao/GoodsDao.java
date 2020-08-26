package com.lv.Eshop.dao;

import com.lv.Eshop.beans.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsDao extends JpaRepository<Goods, Integer> {
    Goods findById(int id);
    Goods findByName(String name);
}
