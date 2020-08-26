package com.lv.Eshop.dao;

import com.lv.Eshop.beans.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersDao extends JpaRepository<Order, String> {
    Order findByOrderNum(String orderNum);
    List<Order> findByUserName(String userName);
}
