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

import com.cisco.epx.course.app.entity.Course;
import com.cisco.epx.course.app.repository.CourseRepository;

@Controller
@RequestMapping("/courses/")
public class CourseController {

	private final CourseRepository courseRepository;

	@Autowired
	public CourseController(CourseRepository courseRepository) {
		this.courseRepository = courseRepository;
	}

	@GetMapping("add")
	public String showSignUpForm(Course course) {
		return "add-course";
	}

	@GetMapping("list")
	public String showUpdateForm(Model model) {
		model.addAttribute("courses", courseRepository.findAll());
		return "index";
	}

	@PostMapping("add")
	public String addCourse(@Valid Course course, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "add-course";
		}

		courseRepository.save(course);
		return "redirect:list";
	}

	@GetMapping("edit/{id}")
	public String showUpdateForm(@PathVariable("id") long id, Model model) {
		Course course = courseRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
		model.addAttribute("course", course);
		return "update-course";
	}

	@GetMapping("view/{id}")
	public String showCourse(@PathVariable("id") long id, Model model) {
		Course course = courseRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
		model.addAttribute("course", course);
		return "view-course";
	}

	@PostMapping("update/{id}")
	public String updateCourse(@PathVariable("id") long id, @Valid Course course, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			course.setId(id);
			return "update-course";
		}

		courseRepository.save(course);
		model.addAttribute("courses", courseRepository.findAll());
		return "index";
	}

	@GetMapping("delete/{id}")
	public String deleteCourse(@PathVariable("id") long id, Model model) {
		Course course = courseRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
		courseRepository.delete(course);
		model.addAttribute("courses", courseRepository.findAll());
		return "index";
	}
}
