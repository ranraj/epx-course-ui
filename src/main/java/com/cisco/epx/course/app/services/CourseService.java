package com.cisco.epx.course.app.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cisco.epx.course.app.model.Course;
import com.cisco.epx.course.app.model.CourseChapter;

@Service
public class CourseService {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${epx.service.url}")
	private String serviceUrl;

	private static final String COURSE = "/course/";
	private static final String CHAPTER = "/chapters/";

	public List<Course> findAll() {
		ResponseEntity<Course[]> response = restTemplate.getForEntity(serviceUrl + "/course", Course[].class);
		return Arrays.asList(response.getBody());
	}

	public void save(@Valid Course course) {
		restTemplate.postForEntity(serviceUrl + "/course", course, Course.class);
	}

	public Optional<Course> findById(String id) {
		ResponseEntity<Course> responseEntity = restTemplate.getForEntity(serviceUrl + COURSE + id, Course.class);
		if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
			return Optional.of(responseEntity.getBody());
		}
		return Optional.empty();
	}

	public void delete(Course course) {
		restTemplate.delete(serviceUrl + COURSE + course.getId());
	}

	public List<CourseChapter> findAllChapters(String courseId) {
		ResponseEntity<CourseChapter[]> response = restTemplate
				.getForEntity(serviceUrl + COURSE + courseId + "/chapters", CourseChapter[].class);
		return Arrays.asList(response.getBody());
	}

	public void addChapter(CourseChapter courseChapter) {
		String endpoint = serviceUrl + "/course/" + courseChapter.getCourseId() + "/chapters";
		restTemplate.postForEntity(endpoint, courseChapter, CourseChapter.class);
	}

	public void deleteChapter(String courseId, String chapterId) {
		restTemplate.delete(serviceUrl + COURSE + courseId + CHAPTER +chapterId);
		
	}
	
	
}
