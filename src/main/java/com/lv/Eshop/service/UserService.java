package com.lv.Eshop.service;

import com.lv.Eshop.beans.ShoppingCart;
import com.lv.Eshop.beans.User;
import com.lv.Eshop.beans.UserProfile;
import com.lv.Eshop.dao.ShoppingCartDao;
import com.lv.Eshop.dao.UserDao;
import com.lv.Eshop.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class UserService {
	@Autowired
	UserDao userDao;

	@Autowired
	ShoppingCartDao shoppingCartDao;

	@Autowired
    PasswordEncoder passwordEncoder;
	
	public Response register(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		List<UserProfile> profiles = new ArrayList<UserProfile>();
		profiles.add(new UserProfile(2));
		user.setProfiles(profiles);
		//shopping carts
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setId(user.getId());
		shoppingCartDao.save(shoppingCart);
		System.out.println(user);
		user.setShoppingCart(shoppingCart);

		userDao.save(user);
		return new Response(true);
	}

	public Response registerSale(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		List<UserProfile> profiles = new ArrayList<UserProfile>();
		profiles.add(new UserProfile(3));
		user.setProfiles(profiles);
		//shopping carts
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setId(user.getId());
		shoppingCartDao.save(shoppingCart);
		System.out.println(user);
		user.setShoppingCart(shoppingCart);

		userDao.save(user);
		return new Response(true);
	}
	
	public Response registerAdm(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		List<UserProfile> profiles = new ArrayList<UserProfile>();
		profiles.add(new UserProfile(1));
		user.setProfiles(profiles);
		//shopping carts
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setId(user.getId());
		shoppingCartDao.save(shoppingCart);
		System.out.println(user);
		user.setShoppingCart(shoppingCart);

		userDao.save(user);

		return new Response(true);
	}
	
	public Response changePassword(User user, Authentication authentication) {
		if(user.getUsername().equals(authentication.getName()) || isAdmin(authentication.getAuthorities())) {
			User u = userDao.findByUsername(user.getUsername());
			u.setPassword(passwordEncoder.encode(user.getPassword()));
			userDao.save(u);
		}else {
			return new Response(false);
		}
		return new Response(true);
	}
	
	public boolean isAdmin(Collection<? extends GrantedAuthority> profiles) {
		boolean isAdmin = false;
		for(GrantedAuthority profle: profiles) {
			if(profle.getAuthority().equals("ROLE_ADMIN")) {
				isAdmin = true;
			}
		};
		return isAdmin;
	}
	
	public Response deleteUser(Authentication authentication) {
		String userName = authentication.getName();
		if(userDao.findByUsername(userName) != null) {
			userDao.deleteByUsername(userName);
			return new Response(true);
		}else {
			return new Response(false, "User is not found!");
		}
	}
}
