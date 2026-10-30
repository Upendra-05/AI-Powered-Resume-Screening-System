package com.airesume.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.airesume.entity.JobApplication;

public interface JobApplicationRepo extends JpaRepository<JobApplication, Long>
{
	List<JobApplication> findByUserId(Long userId);
	List<JobApplication> findByJobId(Long jobId);
	boolean existsByUserIdAndJobId(Long userId, Long jobId);
}
