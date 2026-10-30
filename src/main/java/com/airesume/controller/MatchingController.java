package com.airesume.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.airesume.entity.Job;
import com.airesume.entity.Resume;
import com.airesume.repository.JobRepo;
import com.airesume.repository.ResumeRepo;
import com.airesume.service.SkillMatchingService;

@RestController
@RequestMapping("/api/matching")
@CrossOrigin(origins = "*")
public class MatchingController 
{
    private final ResumeRepo resumeRepo;
    private final JobRepo jobRepo;
    private final SkillMatchingService skillMatchingService;

    public MatchingController(
            ResumeRepo resumeRepo,
            JobRepo jobRepo,
            SkillMatchingService skillMatchingService) 
    {
        this.resumeRepo = resumeRepo;
        this.jobRepo = jobRepo;
        this.skillMatchingService = skillMatchingService;
    }

    @GetMapping("/recommendations/{userId}")
    public List<Map<String, Object>> getRecommendations(@PathVariable Long userId) 
    {
        List<Resume> resumes = resumeRepo.findByUserId(userId);
        if (resumes.isEmpty()) 
        {
            throw new RuntimeException("Please upload a resume before requesting recommendations");
        }
        Resume latestResume = resumes.stream()
                .max(Comparator.comparing(Resume::getId))
                .orElseThrow();
        List<Job> jobs = jobRepo.findAll();
        List<Map<String, Object>> recommendations = new ArrayList<>();
        for (Job job : jobs) 
        {
            double score =  skillMatchingService.calculateMatchScore(
                            latestResume.getExtractedText(),
                            job.getRequiredSkills());

            Map<String, Object> result = new HashMap<>();

            result.put("jobId", job.getId());
            result.put("title", job.getTitle());
            result.put("companyName", job.getCompanyName());
            result.put("location", job.getLocation());
            result.put("requiredSkills", job.getRequiredSkills());
            result.put("salary", job.getSalary());
            result.put("matchScore", score);

            recommendations.add(result);
        }
        recommendations.sort((first, second) ->
                Double.compare(
                        (Double) second.get("matchScore"),
                        (Double) first.get("matchScore")));
        return recommendations;
    }
}