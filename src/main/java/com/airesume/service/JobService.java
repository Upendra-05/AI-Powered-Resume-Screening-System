package com.airesume.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.airesume.entity.Job;
import com.airesume.repository.JobRepo;

@Service
public class JobService
{
	private final JobRepo jobRepo;
	
	public JobService(JobRepo jobRepo)
	{
		this.jobRepo=jobRepo;
	}
	
	public Job createJob(Job job)
	{
		return jobRepo.save(job);
	}
	
	public List<Job> getAllJobs()
	{
		return jobRepo.findAll();
	}
	
	public Job getJobById(Long id)
	{
		return jobRepo.findById(id)
				.orElseThrow(()-> 
						new RuntimeException("Job not found with id : "+id));
	}
	
	public List<Job> searchJobsByTitle(String title)
	{
		return jobRepo.findByTitleContainingIgnoreCase(title);
	}
	
	public List<Job> searchJobsByLocation(String location)
	{
		return jobRepo.findByLocationContainingIgnoreCase(location);
	}
	
	public Job updateJob(Long id, Job updatedJob)
	{
		Job existingJob = getJobById(id);
		
		existingJob.setTitle(updatedJob.getTitle());
        existingJob.setCompanyName(updatedJob.getCompanyName());
        existingJob.setLocation(updatedJob.getLocation());
        existingJob.setRequiredSkills(updatedJob.getRequiredSkills());
        existingJob.setDescription(updatedJob.getDescription());
        existingJob.setSalary(updatedJob.getSalary());
        
        return jobRepo.save(existingJob);
	}
	
	public void deleteJob(Long id)
	{
		Job job = getJobById(id);
		jobRepo.delete(job);
	}
}
























