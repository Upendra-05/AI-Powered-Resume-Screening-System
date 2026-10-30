package com.airesume.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="job_applications")
public class JobApplication 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="user_id",nullable=false)
	private User user;
	
	@ManyToOne
	@JoinColumn(name="job_id",nullable=false)
	private Job job;
	
	private double matchScore;
	private String status;
	
	public JobApplication() 
	{
    }

	public JobApplication(User user, Job job, double matchScore, String status) 
    {
	    this.user = user;
	    this.job = job;
	    this.matchScore = matchScore;
	    this.status = status;
    }
    
	public final Long getId() {
		return id;
	}
	public final void setId(Long id) {
		this.id = id;
	}
	public final User getUser() {
		return user;
	}
	public final void setUser(User user) {
		this.user = user;
	}
	public final Job getJob() {
		return job;
	}
	public final void setJob(Job job) {
		this.job = job;
	}
	public final double getMatchScore() {
		return matchScore;
	}
	public final void setMatchScore(double matchScore) {
		this.matchScore = matchScore;
	}
	public final String getStatus() {
		return status;
	}
	public final void setStatus(String status) {
		this.status = status;
	}
}
