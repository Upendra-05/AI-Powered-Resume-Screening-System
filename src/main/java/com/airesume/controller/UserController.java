package com.airesume.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.airesume.entity.User;
import com.airesume.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins="*")
public class UserController 
{
	private final UserService userService;
	
	public UserController(UserService userService)
	{
		this.userService=userService;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public User registerUser(@RequestBody User user)
	{
		return userService.registerUser(user);
	}
	
	@GetMapping
	public List<User> getAllUsers()
	{
		return userService.getAllUsers();
	}
	
	@GetMapping("/{id}")
	public User getUserById(@PathVariable Long id)
	{
		return userService.getUserById(id);
	}
}
























