package com.lv.Eshop.controller;

import com.lv.Eshop.beans.User;
import com.lv.Eshop.dao.UserDao;
import com.lv.Eshop.http.Response;
import com.lv.Eshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/users")
public class UserController {
	@Autowired
	UserDao userDao;
	
	@Autowired
	UserService userService;
	
	@Autowired
    PasswordEncoder passwordEncoder;
	
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping
	public List<User> getUsers(){
		return userDao.findAll();
	}
	
	@PostMapping("/admin")
	public Response addUserAdmin(@RequestBody User user) {
		return userService.registerAdm(user);
	}

	@PostMapping("/user")
	public Response addUser(@RequestBody User user) {
		return userService.register(user);
	}

	@PostMapping("/sale")
	public Response addUserSale(@RequestBody User user) {
		return userService.registerSale(user);
	}
	
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
	@PutMapping
	public Response changeUser(@RequestBody User user, Authentication authentication) {
		return userService.changePassword(user, authentication);
	}
	
	@DeleteMapping()
	public Response deleteUser( Authentication authentication) {
		return userService.deleteUser(authentication);
	}
}
