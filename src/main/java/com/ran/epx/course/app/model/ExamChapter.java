package com.ran.epx.course.app.model;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors
public class ExamChapter {
	
	private String id;
	private String chapterId;
	private String courseId;
	private float version;
	private String status;	// Iscompleted, inprogress, New
	private List<ExamChapterQuestion> questions;	
	private String userId;
	
}
