package com.cisco.epx.course.app.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final String COURSES_AND_CHAPTERS = "%s/course/%s/chapters";

	@Autowired
	private RestTemplate restTemplate;

	@Value("${epx.service.url}")
	private String serviceUrl;
	
	private static final Logger log = LoggerFactory.getLogger(CourseService.class);
	
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
		String url = String.format(COURSES_AND_CHAPTERS,serviceUrl,courseId);		
		ResponseEntity<CourseChapter[]> response = restTemplate
				.getForEntity(url, CourseChapter[].class);
		return Arrays.asList(response.getBody());
	}

	public void addChapter(CourseChapter courseChapter) {
		updateChapterQuestion(courseChapter);
	}

	public void deleteChapter(String courseId, String chapterId) {
		restTemplate.delete(serviceUrl + COURSE + courseId + CHAPTER +chapterId);
		
	}
	public Optional<CourseChapter> findChapterById(String courseId,String chapterId) {
		String url = String.format("%s/course/%s/chapters/%s",serviceUrl,courseId,chapterId);
		ResponseEntity<CourseChapter> responseEntity = restTemplate.getForEntity(url, CourseChapter.class);
		if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
			return Optional.of(responseEntity.getBody());
		}
		return Optional.empty();
	}
	
	public void updateChapterQuestion(CourseChapter courseChapter) {
		String url = String.format(COURSES_AND_CHAPTERS,serviceUrl,courseChapter.getCourseId());		
		restTemplate.postForEntity(url, courseChapter, CourseChapter.class);
	}
	
}
