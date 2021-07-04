package com.cisco.epx.course.app.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cisco.epx.course.app.model.Course;
import com.cisco.epx.course.app.services.CourseService;

@Controller
@RequestMapping("/courses/")
public class CourseController {

	private final CourseService courseService;

	@Autowired
	public CourseController(CourseService courseRepository) {
		this.courseService = courseRepository;
	}

	@GetMapping("add")
	public String showSignUpForm(Course course) {
		return "add-course";
	}

	@GetMapping("list")
	public String showUpdateForm(Model model) {
		System.out.println(courseService.findAll().size());
		model.addAttribute("courses", courseService.findAll());
		return "index";
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
				.orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
		model.addAttribute("course", course);
		return "update-course";
	}

	@GetMapping("view/{id}")
	public String showCourse(@PathVariable("id") String id, Model model) {
		Course course = courseService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
		model.addAttribute("course", course);
		return "view-course";
	}

	@PostMapping("update/{id}")
	public String updateCourse(@PathVariable("id") String id, @Valid Course course, BindingResult result,
			Model model) {
		if (result.hasErrors()) {			
			return "update-course";
		}

		courseService.save(course);
		model.addAttribute("courses", courseService.findAll());
		return "index";
	}

	@GetMapping("delete/{id}")
	public String deleteCourse(@PathVariable("id") String id, Model model) {
		Course course = courseService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
		courseService.delete(course);
		model.addAttribute("courses", courseService.findAll());
		return "index";
	}
	
	@GetMapping("course/{id}/chapters/radd")
	public String showCourseChapters(@PathVariable("id") String id, Model model) {
		Course course = courseService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
		model.addAttribute("course", course);
		return "update-course";
	}
}
