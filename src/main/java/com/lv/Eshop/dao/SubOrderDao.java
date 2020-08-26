package com.lv.Eshop.dao;

import com.lv.Eshop.beans.SubOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubOrderDao extends JpaRepository<SubOrder, Integer> {
}
