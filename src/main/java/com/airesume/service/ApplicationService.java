package com.airesume.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.airesume.entity.Job;
import com.airesume.entity.JobApplication;
import com.airesume.entity.Resume;
import com.airesume.entity.User;
import com.airesume.repository.JobApplicationRepo;
import com.airesume.repository.JobRepo;
import com.airesume.repository.ResumeRepo;
import com.airesume.repository.UserRepo;

@Service
public class ApplicationService 
{

    private final JobApplicationRepo applicationRepo;
    private final UserRepo userRepo;
    private final JobRepo jobRepo;
    private final ResumeRepo resumeRepo;
    private final SkillMatchingService skillMatchingService;

    public ApplicationService(
            JobApplicationRepo applicationRepo,
            UserRepo userRepo,
            JobRepo jobRepo,
            ResumeRepo resumeRepo,
            SkillMatchingService skillMatchingService) 
    {

        this.applicationRepo = applicationRepo;
        this.userRepo = userRepo;
        this.jobRepo = jobRepo;
        this.resumeRepo = resumeRepo;
        this.skillMatchingService = skillMatchingService;
    }

    public JobApplication applyForJob(Long userId, Long jobId) 
    {
        User user = userRepo.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with ID: " + userId));
        Job job = jobRepo.findById(jobId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Job not found with ID: " + jobId));
        if (applicationRepo
                .existsByUserIdAndJobId(userId, jobId))
        {
            throw new RuntimeException(
                    "You have already applied for this job");
        }
        List<Resume> resumes = resumeRepo.findByUserId(userId);
        if (resumes.isEmpty())
        {
            throw new RuntimeException(
                    "Please upload your resume before applying");
        }
        Resume latestResume = resumes.get(resumes.size() - 1);
        double matchScore =
                skillMatchingService.calculateMatchScore(
                        latestResume.getExtractedText(),
                        job.getRequiredSkills());

        JobApplication application = new JobApplication();

        application.setUser(user);
        application.setJob(job);
        application.setMatchScore(matchScore);
        application.setStatus("APPLIED");

        return applicationRepo.save(application);
    }

    public List<JobApplication> getApplicationsByUserId(Long userId) 
    {
        return applicationRepo.findByUserId(userId);
    }
    
    public List<JobApplication> getApplicationsByJobId(Long jobId) 
    {
        return applicationRepo.findByJobId(jobId);
    }

    public JobApplication updateApplicationStatus(Long applicationId, String status) 
    {
        JobApplication application =
                applicationRepo.findById(applicationId)
                        .orElseThrow(() ->
                                new RuntimeException("Application not found with ID: "+ applicationId));
        application.setStatus(status);

        return applicationRepo.save(application);
    }
}