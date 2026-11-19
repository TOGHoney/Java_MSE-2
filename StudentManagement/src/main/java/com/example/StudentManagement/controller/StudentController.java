package com.example.StudentManagement.controller;

import com.example.StudentManagement.model.Student;
import com.example.StudentManagement.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> addStudent(
            @RequestBody Student student
    ) throws SQLException {

        Student addedStudent = studentService.addStudent(student);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(addedStudent);
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() throws SQLException {

        List<Student> students = studentService.getAllStudents();

        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable int id) throws SQLException {

        Student student = studentService.getStudentById(id);

        return ResponseEntity.ok(student);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudentScoresAndSkills(@PathVariable int id,@RequestBody Student student) throws SQLException {
        Student updatedStudent = studentService.updateStudentScoresAndSkills(id, student);
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable int id) throws SQLException {
        studentService.deleteStudent(id);
        return "Student with id: " + id + "deleted successfully";
    }

    @GetMapping("/merit-list")
    public ResponseEntity<List<Student>> getMeritList() throws SQLException {
        List<Student> meritList = studentService.getMeritList();
        return ResponseEntity.ok(meritList);
    }
}
