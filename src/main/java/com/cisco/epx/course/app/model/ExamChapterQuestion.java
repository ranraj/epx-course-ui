package com.cisco.epx.course.app.model;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors
public class ExamChapterQuestion {
	private int score;
	private String questionId;
	private String answer;
	private List<String> answers;
	private boolean isPass;
	private float version;	
	private ChapterQuestion chapterQuestion;
	private String chapterId;
	
	public static ExamChapterQuestion from(ChapterQuestion chapterQuestion) {
		ExamChapterQuestion examChapterQuestion = new ExamChapterQuestion();
		examChapterQuestion.setQuestionId(chapterQuestion.getId());
		examChapterQuestion.setVersion(chapterQuestion.getVersion());
		examChapterQuestion.setChapterQuestion(chapterQuestion);
		examChapterQuestion.setChapterId(chapterQuestion.getChapterId());
		return examChapterQuestion;
	}
}
