package com.lv.Eshop.dao;

import com.lv.Eshop.beans.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Integer> {
	User findByUsername(String username);
	void deleteByUsername(String username);
}
