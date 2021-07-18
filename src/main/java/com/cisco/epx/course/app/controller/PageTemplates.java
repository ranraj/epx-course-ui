package com.ran.epx.course.app.controller;

public enum PageTemplates {
	INDEX("index"),
	LIST_COURSES("list-courses"),
	ADD_COURSE("add-course"),
	UPDATE_COURSE("update-course"),
	LEARN_COURSE("learn-course"),
	ADD_COURSE_CHAPTER("add-course-chapter"),
	ADD_CHAPTER_QUESTIONS("add-chapter-questions"),
	LEARN_CHAPTER_QUESTIONS("learn-chapter-questions"),
	LEARN_CHAPTER_QUESTIONS_RESULT("learn-chapter-questions-result");
	
	private final String id;

	PageTemplates(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
