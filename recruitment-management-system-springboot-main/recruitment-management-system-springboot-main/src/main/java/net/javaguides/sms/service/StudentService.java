package net.javaguides.sms.service;

import java.util.List;

import net.javaguides.sms.entity.Student;
import org.springframework.data.domain.Sort;

public interface StudentService {
	List<Student> getAllStudents();
	
	Student saveStudent(Student student);
	
	Student getStudentById(Long id);
	
	Student updateStudent(Student student);
	
	void deleteStudentById(Long id);

	List<Student> getAllStudents(Sort by);
}
