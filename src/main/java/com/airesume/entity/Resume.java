package com.airesume.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="resumes")
public class Resume 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private Long id;
	private String fileName;
	private String filePath;
	
	@Column(columnDefinition = "LONGTEXT")
	private String extractedText;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name ="user_id",nullable=false)
	private User user;
	
	public Resume(String fileName, String filePath, String extractedText, User user)
	{
		this.fileName=fileName;
		this.filePath=filePath;
		this.extractedText=extractedText;
		this.user=user;
	}

	public Resume() {
	}

	public final Long getId() {
		return id;
	}

	public final void setId(Long id) {
		this.id = id;
	}

	public final String getFileName() {
		return fileName;
	}

	public final void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public final String getFilePath() {
		return filePath;
	}

	public final void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public final String getExtractedText() {
		return extractedText;
	}

	public final void setExtractedText(String extractedText) {
		this.extractedText = extractedText;
	}

	public final User getUser() {
		return user;
	}

	public final void setUser(User user) {
		this.user = user;
	}
}

