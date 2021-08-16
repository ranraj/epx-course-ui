package com.ran.epx.course.app.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.PingHealthIndicator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ran.epx.course.app.config.AppConstant;
import com.ran.epx.course.app.dto.LikeCourseDto;
import com.ran.epx.course.app.model.AnswerType;
import com.ran.epx.course.app.model.ChapterContent;
import com.ran.epx.course.app.model.ChapterQuestion;
import com.ran.epx.course.app.model.ContentProvider;
import com.ran.epx.course.app.model.Course;
import com.ran.epx.course.app.model.CourseChapter;
import com.ran.epx.course.app.model.ExamChapter;
import com.ran.epx.course.app.services.CourseService;
import com.ran.epx.course.app.services.UserService;

@Controller
@RequestMapping("/courses/")
public class CourseController {

	private static final String CHAPTERS = "chapters";

	private static final String CHAPTER = "chapter";

	private static final Logger log = LoggerFactory.getLogger(CourseService.class);
	
	private static final String INVALID_COURSE_ID = "Invalid course Id:";
	private static final String COURSES = "courses";
	private static final String COURSE = "course";
	
	@Autowired
	private CourseService courseService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private PingHealthIndicator pingHealthIndicator;
	
	@GetMapping("add")
	public String showSignUpForm(Course course) {
		return PageTemplates.ADD_COURSE.getId();
	}

	@GetMapping
	public String index(Model model) {
		Health health = pingHealthIndicator.getHealth(false);
		log.info("Wakeup call to service, Status : {}",health.getStatus());
		return PageTemplates.INDEX.getId();
	}

	@GetMapping("list")
	public String showListCourses(Model model,HttpServletRequest request) {
		model.addAttribute(COURSES, courseService.findAll());
		String userId = getSessionStoredUserId(request);
		model.addAttribute("userId",userId);
		return PageTemplates.LIST_COURSES.getId();
	}

	@PostMapping("add")
	public String addCourse(@Valid Course course, BindingResult result, Model model,HttpServletRequest request) {
		if (result.hasErrors()) {
			return PageTemplates.ADD_COURSE.getId();
		}
		String userId = getSessionStoredUserId(request);
		course.setOwnerId(userId);
		courseService.save(course);
		return "redirect:list";
	}

	private String getSessionStoredUserId(HttpServletRequest request){
		String userId = (String)request.getSession().getAttribute(AppConstant.USER_ID);
		return userId;
	}
	@GetMapping("edit/{id}")
	public String showUpdateForm(@PathVariable("id") String id, Model model) {
		Course course = courseService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException(INVALID_COURSE_ID + id));
		model.addAttribute(COURSE, course);
		return PageTemplates.UPDATE_COURSE.getId();
	}

	/**
	 * Learn course for end point for Leaner
	 * 
	 * @param principal
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("view/{id}")
	public String learnCourse(Principal principal, Authentication authentication, @PathVariable("id") String id,
			Model model, HttpServletRequest request, HttpServletResponse response) {
		Course course = courseService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException(INVALID_COURSE_ID + id));
		model.addAttribute(COURSE, course);
		model.addAttribute(CHAPTERS, courseService.findAllChapters(id));
		String userId = getSessionStoredUserId(request);
		
		LikeCourseDto likeCourse = new LikeCourseDto(course.getId(),userId);
		model.addAttribute("likeCourse", likeCourse);
		
		boolean liked = false;
		if(course.getLikedBy()!= null) {
			liked = course.getLikedBy().contains(userId);
		}
				
		model.addAttribute("liked",liked);
		
		return PageTemplates.LEARN_COURSE.getId();
	}

	@PostMapping("update/{id}")
	public String updateCourse(@PathVariable("id") String id, @Valid Course course, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return PageTemplates.UPDATE_COURSE.getId();
		}

		courseService.save(course);
		model.addAttribute(COURSES, courseService.findAll());
		return PageTemplates.INDEX.getId();
	}

	@GetMapping("delete/{id}")
	public String deleteCourse(@PathVariable("id") String id, Model model) {
		Course course = courseService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException(INVALID_COURSE_ID + id));
		courseService.delete(course);
		model.addAttribute(COURSES, courseService.findAll());
		return PageTemplates.INDEX.getId();
	}

	@GetMapping("view/{courseId}/chapters/add")
	public String showCourseChapters(@PathVariable("courseId") String courseId, Model model) {
		Course course = courseService.findById(courseId)
				.orElseThrow(() -> new IllegalArgumentException(INVALID_COURSE_ID + courseId));
		model.addAttribute(COURSE, course);

		model.addAttribute(CHAPTERS, courseService.findAllChapters(courseId));
		CourseChapter chapter = new CourseChapter();
		chapter.setContent(new ChapterContent());
		model.addAttribute(CHAPTER, chapter);
		return PageTemplates.ADD_COURSE_CHAPTER.getId();
	}

	@PostMapping("view/{courseId}/chapters/add")
	public String addCourseChapter(@Valid CourseChapter courseChapter, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "add-course-chapter";
		}		
		//Currently supporting only Youtube
		courseChapter.setContentProvider(ContentProvider.YOUTUBE);
		courseService.addChapter(courseChapter);
		return "redirect:/courses/view/" + courseChapter.getCourseId() + "/chapters/add";
	}

	@GetMapping("view/{courseId}/chapters/delete/{chapterId}")
	public String deleteCourseChapter(@PathVariable("courseId") String courseId,
			@PathVariable("chapterId") String chapterId, Model model) {
		courseService.findById(courseId).orElseThrow(() -> new IllegalArgumentException(INVALID_COURSE_ID + courseId));

		courseService.deleteChapter(courseId, chapterId);
		return "redirect:/courses/view/" + courseId + "/chapters/add";
	}

	@GetMapping("/view/{courseId}/chapters/{chapterId}/questions/add")
	public String showCourseChapters(@PathVariable("courseId") String courseId,
			@PathVariable("chapterId") String chapterId, Model model) {
		Course course = courseService.findById(courseId)
				.orElseThrow(() -> new IllegalArgumentException(INVALID_COURSE_ID + courseId));
		CourseChapter courseChapter = courseService.findChapterById(courseId, chapterId).orElse(new CourseChapter());

		model.addAttribute(COURSE, course);

		model.addAttribute(CHAPTER, courseChapter);
		model.addAttribute("answerTypes", Arrays.asList(AnswerType.values()));
		
		ChapterQuestion question = new ChapterQuestion();
		question.setId(UUID.randomUUID().toString());
		model.addAttribute("question",question);
		return PageTemplates.ADD_CHAPTER_QUESTIONS.getId();
	}

	@PostMapping("view/{courseId}/chapters/{chapterId}/questions/add")
	public String addQuestionsToChapter(@PathVariable("courseId") String courseId,
			@PathVariable("chapterId") String chapterId, @Valid ChapterQuestion examQuestion, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			return PageTemplates.ADD_CHAPTER_QUESTIONS.getId();
		}
		CourseChapter courseChapter = courseService.findChapterById(courseId, chapterId)
				.orElseThrow(() -> new IllegalArgumentException(INVALID_COURSE_ID + courseId));

		if (courseChapter.getExamQuestions() == null) {
			courseChapter.setExamQuestions(new ArrayList<ChapterQuestion>());
		}

		courseChapter.getExamQuestions().add(examQuestion);
		courseService.updateChapterQuestion(courseChapter);
		return "redirect:/courses/view/" + courseId + "/chapters/" + chapterId + "/questions/add";
	}

	// TODO : Soft delete Question

	@GetMapping("/learn/{courseId}/chapters/{chapterId}/exam")
	public String showChaptersQuestionsView(Principal principal, @PathVariable("courseId") String courseId,
			@PathVariable("chapterId") String chapterId, Model model) {
		Course course = courseService.findById(courseId)
				.orElseThrow(() -> new IllegalArgumentException(INVALID_COURSE_ID + courseId));
		CourseChapter courseChapter = courseService.findChapterById(courseId, chapterId).orElse(new CourseChapter());

		model.addAttribute(COURSE, course);

		model.addAttribute(CHAPTER, courseChapter);

		
		return PageTemplates.LEARN_CHAPTER_QUESTIONS.getId();
	}

	@PostMapping("learn/{courseId}/chapters/{chapterId}/exam")
	public String saveExamAnswer(Principal principal, @PathVariable("courseId") String courseId,
			@PathVariable("chapterId") String chapterId, ExamChapter examChapter, Model model, HttpServletRequest request) {

		String userId = (String)request.getSession().getAttribute(AppConstant.USER_ID);		
		ExamChapter examResult = userService.submitExam(userId,examChapter);
		
		//Response page
		Course course = courseService.findById(courseId)
				.orElseThrow(() -> new IllegalArgumentException(INVALID_COURSE_ID + courseId));
		CourseChapter courseChapter = courseService.findChapterById(courseId, chapterId).orElse(new CourseChapter());
		
		OptionalDouble scoreOpt = OptionalDouble.empty();
		int total = 0;
		if(examResult!=null) {
			scoreOpt  = examResult.getQuestions().stream().mapToDouble(a -> a.getScore()).average();
			
			total = examResult.getQuestions().size();
		}
		model.addAttribute("score", scoreOpt.orElse(0.0D) * 100);
		model.addAttribute("noOfQuestions", total);
		
		model.addAttribute(COURSE, course);

		model.addAttribute(CHAPTER, courseChapter);
		return PageTemplates.LEARN_CHAPTER_QUESTIONS_RESULT.getId();
	}
	
	// TODO : List all result
//	@GetMapping("/learn/{courseId}/chapters/{chapterId}/exam/result")
//	public String showExamResultView(Principal principal, @PathVariable("courseId") String courseId,
//			@PathVariable("chapterId") String chapterId, Model model) {
//		Course course = courseService.findById(courseId)
//				.orElseThrow(() -> new IllegalArgumentException(INVALID_COURSE_ID + courseId));
//		CourseChapter courseChapter = courseService.findChapterById(courseId, chapterId).orElse(new CourseChapter());
//		
//		
//		model.addAttribute(COURSE, course);
//
//		model.addAttribute("chapter", courseChapter);
//		
//		return "learn-chapter-questions-result";
//	}
	
	@PostMapping("like")
	public String likeCourse(@Valid LikeCourseDto likeCourseDto) {
		 
		courseService.likeCourse(likeCourseDto);
		return "redirect:list";
	}
	@PostMapping("unlike")
	public String unlikeCourse(@Valid LikeCourseDto likeCourseDto) {
		 
		courseService.unlikeCourse(likeCourseDto);
		return "redirect:list";
	}
}
