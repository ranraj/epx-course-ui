package com.cisco.epx.course.app.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cisco.epx.course.app.model.AnswerType;
import com.cisco.epx.course.app.model.Course;
import com.cisco.epx.course.app.model.CourseChapter;
import com.cisco.epx.course.app.model.ExamQuestion;
import com.cisco.epx.course.app.services.CourseService;

@Controller
@RequestMapping("/courses/")
public class CourseController {

	private static final String INVALID_COURSE_ID = "Invalid course Id:";
	private static final String COURSES = "courses";
	private static final String COURSE = "course";
	private final CourseService courseService;

	@Autowired
	public CourseController(CourseService courseRepository) {
		this.courseService = courseRepository;
	}

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
		return "view-courses";
	}

	@PostMapping("add")
	public String addCourse(@Valid Course course, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "add-course";
		}

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

	@GetMapping("view/{id}")
	public String showCourse(@PathVariable("id") String id, Model model) {
		Course course = courseService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException(INVALID_COURSE_ID + id));
		model.addAttribute(COURSE, course);
		model.addAttribute("chapters", courseService.findAllChapters(id));		
		return "learn-course";
	}

	@PostMapping("update/{id}")
	public String updateCourse(@PathVariable("id") String id, @Valid Course course, BindingResult result,
			Model model) {
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
		return "redirect:/courses/view/"+courseChapter.getCourseId()+"/chapters/add";
	}
	
	@GetMapping("view/{courseId}/chapters/delete/{chapterId}")
	public String deleteCourseChapter(@PathVariable("courseId") String courseId,@PathVariable("chapterId") String chapterId, Model model) {
		courseService.findById(courseId)
				.orElseThrow(() -> new IllegalArgumentException(INVALID_COURSE_ID + courseId));
		 
		courseService.deleteChapter(courseId,chapterId);		
		return "redirect:/courses/view/"+courseId+"/chapters/add";
	}
	

	@GetMapping("/view/{courseId}/chapters/{chapterId}/questions/add")
	public String showCourseChapters(@PathVariable("courseId") String courseId,@PathVariable("chapterId") String chapterId, Model model) {
		Course course = courseService.findById(courseId)
				.orElseThrow(() -> new IllegalArgumentException(INVALID_COURSE_ID + courseId));
		CourseChapter courseChapter = courseService.findChapterById(courseId,chapterId).orElse(new CourseChapter());
		 		
		model.addAttribute(COURSE, course);
				 
		model.addAttribute("chapter",courseChapter);
		model.addAttribute("answerTypes",Arrays.asList(AnswerType.values()));
		model.addAttribute("question", new ExamQuestion());
		return "add-chapter-questions";
	}
	@PostMapping("view/{courseId}/chapters/{chapterId}/questions/add")
	public String addQuestionsToChapter(@PathVariable("courseId") String courseId,@PathVariable("chapterId") String chapterId,@Valid ExamQuestion examQuestion, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "add-chapter-questions";
		}
		CourseChapter courseChapter = courseService.findChapterById(courseId,chapterId).orElseThrow(() -> new IllegalArgumentException(INVALID_COURSE_ID + courseId));
		
		if(courseChapter.getExamQuestions() == null) {
			courseChapter.setExamQuestions(new ArrayList<ExamQuestion>());
		}
		
		courseChapter.getExamQuestions().add(examQuestion);
		courseService.updateChapterQuestion(courseChapter);
		return "redirect:/courses/view/"+courseId+"/chapters/"+chapterId+"/questions/add";
	}
//	
//	@GetMapping("view/{courseId}/chapters/delete/{chapterId}")
//	public String deleteCourseChapter(@PathVariable("courseId") String courseId,@PathVariable("chapterId") String chapterId, Model model) {
//		courseService.findById(courseId)
//				.orElseThrow(() -> new IllegalArgumentException(INVALID_COURSE_ID + courseId));
//		 
//		courseService.deleteChapter(courseId,chapterId);		
//		return "redirect:/courses/view/"+courseId+"/chapters/add";
//	}
	
	 
}
