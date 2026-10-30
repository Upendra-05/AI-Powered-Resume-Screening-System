package com.airesume.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.airesume.entity.Resume;
import com.airesume.service.ResumeService;

@RestController
@RequestMapping("/api/resumes")
@CrossOrigin(origins = "*")
public class ResumeController 
{
    private final ResumeService resumeService;
    public ResumeController(ResumeService resumeService) 
    {
        this.resumeService = resumeService;
    }

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public Resume uploadResume( @RequestParam Long userId, @RequestParam("file") MultipartFile file)throws IOException 
    {
        return resumeService.uploadResume(userId, file);
    }

    @GetMapping("/user/{userId}")
    public List<Resume> getResumesByUserId(@PathVariable Long userId)
    {
        return resumeService.getResumesByUserId(userId);
    }

    @GetMapping("/{resumeId}")
    public Resume getResumeById(@PathVariable Long resumeId) 
    {
        return resumeService.getResumeById(resumeId);
    }
}