package com.cisco.epx.course.app.services;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cisco.epx.course.app.model.ExamChapter;
import com.cisco.epx.course.app.model.User;

@Service
public class UserService {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${epx.service.url}")
	private String serviceUrl;	
			
	public Optional<User> findByEmail(String emailId) {
		String url = String.format("%s/user/search?emailId=%s",serviceUrl,emailId);
		ResponseEntity<User> responseEntity = restTemplate.getForEntity(url, User.class);
		if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
			return Optional.of(responseEntity.getBody());
		}
		return Optional.empty();
	}
	
//	public void updateChapterQuestion(CourseChapter courseChapter) {
//		String url = String.format("%s/course/%s/chapters",serviceUrl,courseChapter.getCourseId());		
//		restTemplate.postForEntity(url, courseChapter, CourseChapter.class);
//	}

	public void register(@Valid User user) {
		String url = String.format("%s/user",serviceUrl);		
		restTemplate.postForEntity(url, user, User.class);		
	}

	public void submitExam(String userId, ExamChapter examChapter) {
		findByUserId(userId).orElseThrow(()-> new IllegalArgumentException("Invalid User"));
		String url = String.format("%s/exams/users/%s",serviceUrl,userId);
		
		ResponseEntity<ExamChapter> response = restTemplate.postForEntity(url, examChapter, ExamChapter.class);
		if(response.getStatusCode() != HttpStatus.OK) {
			throw new IllegalArgumentException("Unable to submit exam");
		}
	}
	public Optional<User> findByUserId(String userId){
		String url = String.format("%s/user/%s",serviceUrl,userId);
		
		ResponseEntity<User> responseEntity = restTemplate.getForEntity(url, User.class);
		if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
			return Optional.of(responseEntity.getBody());
		}
		return Optional.empty();
	}
	
	
}
