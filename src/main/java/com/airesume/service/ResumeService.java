package com.airesume.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.airesume.entity.Resume;
import com.airesume.entity.User;
import com.airesume.repository.ResumeRepo;
import com.airesume.repository.UserRepo;

@Service
public class ResumeService 
{

    private final ResumeRepo resumeRepository;
    private final UserRepo userRepository;

    @Value("${app.upload.dir:uploads}")
    private String uploadDirectory;

    public ResumeService(
            ResumeRepo resumeRepository,
            UserRepo userRepository)
    {

        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
    }

    public Resume uploadResume(Long userId, MultipartFile file)
            throws IOException 
    {

        if (file == null || file.isEmpty()) 
        {
            throw new RuntimeException("Please select a PDF resume");
        }

        String originalFileName = file.getOriginalFilename();

        if (originalFileName == null
                || !originalFileName.toLowerCase()
                        .endsWith(".pdf")) 
        {

            throw new RuntimeException("Only PDF files are allowed");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with ID: " + userId));

        Path uploadPath = Paths.get(uploadDirectory)
                .toAbsolutePath()
                .normalize();

        Files.createDirectories(uploadPath);

        String storedFileName =
                UUID.randomUUID() + ".pdf";

        Path destination = uploadPath
                .resolve(storedFileName)
                .normalize();

        if (!destination.startsWith(uploadPath))
        {
            throw new RuntimeException("Invalid file path");
        }

        Files.copy(file.getInputStream(), destination);

        String extractedText;

        try (PDDocument document =
                     Loader.loadPDF(destination.toFile())) 
        {

            PDFTextStripper stripper = new PDFTextStripper();
            extractedText = stripper.getText(document);

        } catch (IOException exception) 
        {

            Files.deleteIfExists(destination);

            throw new IOException(
                    "Unable to read the PDF resume", exception);
        }

        Resume resume = new Resume();

        resume.setFileName(originalFileName);
        resume.setFilePath(destination.toString());
        resume.setExtractedText(extractedText);
        resume.setUser(user);

        return resumeRepository.save(resume);
    }

    public List<Resume> getResumesByUserId(Long userId) 
    {
        return resumeRepository.findByUserId(userId);
    }

    public Resume getResumeById(Long resumeId) 
    {
        return resumeRepository.findById(resumeId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Resume not found with ID: " + resumeId));
    }
}