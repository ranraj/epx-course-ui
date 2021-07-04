package com.cisco.epx.course.app.model;

import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors
public class CourseChapter {	
	@Id
    private String id;   
	private String name;
    private String title;
    private String videoLink;
    private List<String> tags;                  
    private List<ExamQuestion> examQuestions;   
    private float version;
    private boolean deleted;
    private String courseId;
}