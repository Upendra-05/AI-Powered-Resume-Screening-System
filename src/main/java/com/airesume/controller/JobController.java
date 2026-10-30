package com.airesume.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.airesume.entity.Job;
import com.airesume.service.JobService;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins="*")
public class JobController 
{
	private final JobService jobService;
	
	public JobController(JobService jobService)
	{
		this.jobService=jobService;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Job createJob(@RequestBody Job job)
	{
		return jobService.createJob(job);
	}
	
	@GetMapping
	public List<Job> getAllJobs()
	{
		return jobService.getAllJobs();
	}
	@GetMapping("/{id}")
	public Job getJobById(@PathVariable Long id) 
	{
        return jobService.getJobById(id);
    }
	
	@GetMapping("/search/title")
	public List<Job> searchByTitle(@RequestParam String title) 
	{
		return jobService.searchJobsByTitle(title);
	}

	@GetMapping("/search/location")
	public List<Job> searchByLocation(@RequestParam String location) 
	{
	    return jobService.searchJobsByLocation(location);
	}

	@PutMapping("/{id}")
	public Job updateJob (@PathVariable Long id, @RequestBody Job job)
	{
	    return jobService.updateJob(id, job);
    }
	
    @DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteJob(@PathVariable Long id) 
    {
        jobService.deleteJob(id);
    }
	   
}
