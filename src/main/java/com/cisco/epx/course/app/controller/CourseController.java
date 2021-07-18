package com.cisco.epx.course.app.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cisco.epx.course.app.config.AppConstant;
import com.cisco.epx.course.app.model.AnswerType;
import com.cisco.epx.course.app.model.ChapterQuestion;
import com.cisco.epx.course.app.model.Course;
import com.cisco.epx.course.app.model.CourseChapter;
import com.cisco.epx.course.app.model.ExamChapter;
import com.cisco.epx.course.app.services.CourseService;
import com.cisco.epx.course.app.services.UserService;

@Controller
@RequestMapping("/courses/")
public class CourseController {

	private static final String INVALID_COURSE_ID = "Invalid course Id:";
	private static final String COURSES = "courses";
	private static final String COURSE = "course";

	@Autowired
	private CourseService courseService;

	@Autowired
	private UserService userService;

	@GetMapping("add")
	public String showSignUpForm(Course course) {
		return "add-course";
	}

	@GetMapping
	public String index(Model model) {

		return "index";
	}

	@GetMapping("list")
	public String showUpdateForm(Model model) {
		model.addAttribute(COURSES, courseService.findAll());
		return "list-courses";
	}

	@PostMapping("add")
	public String addCourse(@Valid Course course, BindingResult result, Model model,HttpServletRequest request) {
		if (result.hasErrors()) {
			return "add-course";
		}
		String userId = (String)request.getSession().getAttribute(AppConstant.USER_ID);	
		course.setOwnerId(userId);
		courseService.save(course);
		return "redirect:list";
	}

	@GetMapping("edit/{id}")
	public String showUpdateForm(@PathVariable("id") String id, Model model) {
		Course course = courseService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException(INVALID_COURSE_ID + id));
		model.addAttribute(COURSE, course);
		return "update-course";
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
	public String showCourse(Principal principal, Authentication authentication, @PathVariable("id") String id,
			Model model, HttpServletRequest request, HttpServletResponse response) {
		Course course = courseService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException(INVALID_COURSE_ID + id));
		model.addAttribute(COURSE, course);
		model.addAttribute("chapters", courseService.findAllChapters(id));

		return "learn-course";
	}

	@PostMapping("update/{id}")
	public String updateCourse(@PathVariable("id") String id, @Valid Course course, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "update-course";
		}

		courseService.save(course);
		model.addAttribute(COURSES, courseService.findAll());
		return "index";
	}

	@GetMapping("delete/{id}")
	public String deleteCourse(@PathVariable("id") String id, Model model) {
		Course course = courseService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException(INVALID_COURSE_ID + id));
		courseService.delete(course);
		model.addAttribute(COURSES, courseService.findAll());
		return "index";
	}

	@GetMapping("view/{courseId}/chapters/add")
	public String showCourseChapters(@PathVariable("courseId") String courseId, Model model) {
		Course course = courseService.findById(courseId)
				.orElseThrow(() -> new IllegalArgumentException(INVALID_COURSE_ID + courseId));
		model.addAttribute(COURSE, course);

		model.addAttribute("chapters", courseService.findAllChapters(courseId));
		model.addAttribute("chapter", new CourseChapter());
		return "add-course-chapter";
	}

	@PostMapping("view/{courseId}/chapters/add")
	public String addCourse(@Valid CourseChapter courseChapter, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "add-course-chapter";
		}

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

		model.addAttribute("chapter", courseChapter);
		model.addAttribute("answerTypes", Arrays.asList(AnswerType.values()));
		ChapterQuestion question = new ChapterQuestion();
		question.setId(UUID.randomUUID().toString());
		model.addAttribute("question",question);
		return "add-chapter-questions";
	}

	@PostMapping("view/{courseId}/chapters/{chapterId}/questions/add")
	public String addQuestionsToChapter(@PathVariable("courseId") String courseId,
			@PathVariable("chapterId") String chapterId, @Valid ChapterQuestion examQuestion, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			return "add-chapter-questions";
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

		model.addAttribute("chapter", courseChapter);

		model.addAttribute("answerSheet", courseChapter.getExamChapter());

		return "learn-chapter-questions";
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
		
		double score = 0.0D;
		int total = 0;
		if(examResult!=null) {
			score = examResult.getQuestions().stream().map(a -> a.getScore()).reduce(0.0D, (a,x) -> a+x);
			total = examResult.getQuestions().size();
		}
		model.addAttribute("score", score);
		model.addAttribute("totalMarks", total);
		
		model.addAttribute(COURSE, course);

		model.addAttribute("chapter", courseChapter);
		return "learn-chapter-questions-result";
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
}
