package com.airesume.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.airesume.entity.User;

public interface UserRepo extends JpaRepository<User, Long>
{
	Optional<User> findByEmail(String email);
	
	boolean existsByEmail(String email);
}
