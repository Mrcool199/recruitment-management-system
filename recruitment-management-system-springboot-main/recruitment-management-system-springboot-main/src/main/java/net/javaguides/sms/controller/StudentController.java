package net.javaguides.sms.controller;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import net.javaguides.sms.entity.Student;
import net.javaguides.sms.service.StudentService;

import java.util.List;

@Controller
public class StudentController {
	private StudentService studentService;

	public StudentController(StudentService studentService) {
		super();
		this.studentService = studentService;
	}

	@GetMapping("/students")
	public String listStudents(@RequestParam(name = "sort", required = false) String sort,
							   Model model) {
		Sort.Direction sortDirection = Sort.Direction.ASC; // Default sorting direction
		String sortBy = "id"; // Default sorting field

		if (sort != null && !sort.isEmpty()) {
			String[] sortParams = sort.split(",");
			if (sortParams.length == 2) {
				sortBy = sortParams[0];
				String sortOrder = sortParams[1];
				sortDirection = getSortDirection(sortOrder);
			}
		}

		List<Student> students = studentService.getAllStudents(Sort.by(sortDirection, sortBy));
		model.addAttribute("students", students);
		return "students";
	}

	private Sort.Direction getSortDirection(String sortOrder) {
		if ("asc".equalsIgnoreCase(sortOrder)) {
			return Sort.Direction.ASC;
		} else if ("desc".equalsIgnoreCase(sortOrder)) {
			return Sort.Direction.DESC;
		}
		throw new IllegalArgumentException("Invalid sort order: " + sortOrder);
	}
	@GetMapping("/students/new")
	public String createStudentForm(Model model) {

		// create student object to hold student form data
		Student student = new Student();
		model.addAttribute("student", student);
		return "create_student";
	}

	@PostMapping("/students")
	public String saveStudent(@ModelAttribute("student") Student student) {
		studentService.saveStudent(student);
		return "redirect:/students";
	}

	@GetMapping("/students/edit/{id}")
	public String editStudentForm(@PathVariable Long id, Model model) {
		model.addAttribute("student", studentService.getStudentById(id));
		return "edit_student";
	}

	@GetMapping("/students/complete/{id}")
	public String completeStudentForm(@PathVariable Long id, Model model) {
		Student student = studentService.getStudentById(id);

		if(student.getCompleted() == "Complete"){
			student.setCompleted("Incomplete");
			student.setColorButton("btn btn-warning");
		}
		else{
			student.setCompleted("Complete");
			student.setColorButton("btn btn-success");
		}
		studentService.saveStudent(student);

		return "redirect:/students";
	}

	@PostMapping("/students/{id}")
	public String updateStudent(@PathVariable Long id,
								@ModelAttribute("student") Student student,
								Model model) {

		// get student from database by id
		Student existingStudent = studentService.getStudentById(id);
		existingStudent.setId(id);
		existingStudent.setFirstName(student.getFirstName());
		existingStudent.setLastName(student.getLastName());
		existingStudent.setEmail(student.getEmail());
		existingStudent.setCompleted(student.getCompleted());
		existingStudent.setColorButton(student.getColorButton());

		// save updated student object
		studentService.updateStudent(existingStudent);
		return "redirect:/students";
	}

	@GetMapping("/students/{id}")
	public String deleteStudent(@PathVariable Long id) {
		studentService.deleteStudentById(id);
		return "redirect:/students";
	}
	@GetMapping("/students/sort")
	public String sortStudents(@RequestParam(name = "criteria") String sortingCriteria, Model model) {
		Sort.Direction sortDirection = Sort.Direction.ASC;

		if ("name".equalsIgnoreCase(sortingCriteria)) {
			sortingCriteria = "firstName"; // Map 'name' to the appropriate field in your Student entity (firstName, lastName, etc.)
		}
		else if ("lastName".equalsIgnoreCase(sortingCriteria)) {
			sortingCriteria = "lastName"; // Map 'name' to the appropriate field in your Student entity (firstName, lastName, etc.)
		}

		List<Student> students = studentService.getAllStudents(Sort.by(sortDirection, sortingCriteria));
		model.addAttribute("students", students);
		return "students";
	}
}