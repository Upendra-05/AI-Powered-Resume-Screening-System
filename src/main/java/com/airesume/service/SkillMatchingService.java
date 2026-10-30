package com.airesume.service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class SkillMatchingService 
{
	public double calculateMatchScore(String resumeText, String requiredSkills)
	{
		if(resumeText == null || resumeText.isBlank() || requiredSkills == null || requiredSkills.isBlank())
		{
			return 0.0;
		}
		
		List<String> skills = Arrays.stream(requiredSkills.split(","))
				.map(String::trim)
				.filter(skill -> !skill.isEmpty())
				.map(skill -> skill.toLowerCase(Locale.ROOT))
				.distinct()
				.collect(Collectors.toList());
		
		if(skills.isEmpty())
		{
			return 0.0;
		}
		
		String normalizedResume = resumeText.toLowerCase(Locale.ROOT);
		int matchedSkills = 0;
		
		for(String skill : skills)
		{
			if(normalizedResume.contains(skill))
			{
				matchedSkills++;
			}
		}
		
		double score = (matchedSkills * 100.0)/ skills.size();
		return Math.round(score * 100.0)/100.0;
	}
}
