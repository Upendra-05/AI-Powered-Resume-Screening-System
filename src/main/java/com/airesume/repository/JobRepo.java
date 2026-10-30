package com.airesume.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.airesume.entity.Job;

public interface JobRepo extends JpaRepository<Job, Long> 
{
	List<Job> findByTitleContainingIgnoreCase(String title);
	List<Job> findByLocationContainingIgnoreCase(String location);
	List<Job> findByCompanyNameContainingIgnoreCase(String CompanyName);
}
