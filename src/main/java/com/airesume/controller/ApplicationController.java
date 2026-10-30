package com.airesume.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.airesume.entity.JobApplication;
import com.airesume.service.ApplicationService;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*")
public class ApplicationController
{

    private final ApplicationService applicationService;
    
    public ApplicationController(
            ApplicationService applicationService)
    {
        this.applicationService = applicationService;
    }

    @PostMapping
    public JobApplication applyForJob(@RequestParam Long userId, @RequestParam Long jobId)
    {
        return applicationService.applyForJob(userId, jobId);
    }

    @GetMapping("/user/{userId}")
    public List<JobApplication> getApplicationsByUser(@PathVariable Long userId) 
    {
        return applicationService.getApplicationsByUserId(userId);
    }

    @GetMapping("/job/{jobId}")
    public List<JobApplication> getApplicationsByJob(@PathVariable Long jobId) 
    {
        return applicationService.getApplicationsByJobId(jobId);
    }

    @PutMapping("/{applicationId}/status")
    public JobApplication updateStatus(@PathVariable Long applicationId,@RequestParam String status) 
    {
        return applicationService.updateApplicationStatus(applicationId, status);
    }
}