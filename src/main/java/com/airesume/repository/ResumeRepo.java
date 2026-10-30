package com.airesume.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.airesume.entity.Resume;

public interface ResumeRepo extends JpaRepository<Resume, Long>
{
	List<Resume> findByUserId(Long userId);
}
