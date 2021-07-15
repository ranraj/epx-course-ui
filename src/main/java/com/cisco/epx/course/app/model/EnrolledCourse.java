package com.cisco.epx.course.app.model;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors
public class EnrolledCourse {		
	private String id;
	private String courseId;
	private List<ExamChapter> enrolledChapter;
}
