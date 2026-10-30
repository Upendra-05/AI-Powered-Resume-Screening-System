package com.airesume.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.airesume.entity.User;
import com.airesume.repository.UserRepo;

@Service
public class UserService 
{
	private final UserRepo userRepo;
	
	public UserService(UserRepo userRepo)
	{
		this.userRepo=userRepo;
	}
	
	public User registerUser(User user)
	{
		if(userRepo.existsByEmail(user.getEmail()))
		{
			throw new RuntimeException("Email is already registered");
		}
		return userRepo.save(user);
	}
	
	public List<User> getAllUsers()
	{
		return userRepo.findAll();
	}
	
	public User getUserById(Long id)
	{
		return userRepo.findById(id).orElseThrow(()-> new RuntimeException("User not found with ID : "+id));
				
	}
}
