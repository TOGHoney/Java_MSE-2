package com.example.StudentManagement.service;

import com.example.StudentManagement.model.Student;
import com.example.StudentManagement.repository.Studentdb;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class StudentService {

    private final Studentdb studentDAO;

    public StudentService(Studentdb studentDAO) {
        this.studentDAO = studentDAO;
    }

    public Student addStudent(Student student) throws SQLException {

        studentDAO.addStudent(student);

        return student;
    }

    public List<Student> getAllStudents() throws SQLException {
        return studentDAO.getAllStudents();
    }

    public Student getStudentById(int studentId) throws SQLException {

        Student student = studentDAO.getStudentById(studentId);

        if (student == null) {
            throw new IllegalArgumentException(
                    "Student not found with ID: " + studentId
            );
        }

        return student;
    }

    public Student updateStudentScoresAndSkills(
            int studentId,
            Student updatedStudent
    ) throws SQLException {

        Student existingStudent = getStudentById(studentId);

        existingStudent.setProgrammingScore(
                updatedStudent.getProgrammingScore()
        );

        existingStudent.setMathematicsScore(
                updatedStudent.getMathematicsScore()
        );

        existingStudent.setTechnicalSkills(
                updatedStudent.getTechnicalSkills()
        );

        studentDAO.updateStudentScoresAndSkills(existingStudent);

        return existingStudent;
    }

    public void deleteStudent(int studentId) throws SQLException {

        getStudentById(studentId);
        studentDAO.deleteStudent(studentId);
    }

    public List<Student> getMeritList() throws SQLException {
        return studentDAO.getMeritList();
    }

}