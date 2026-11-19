package com.example.StudentManagement.repository;

import com.example.StudentManagement.model.Student;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class Studentdb {

    private static final String URL =
            "jdbc:mysql://localhost:3306/student_management";

    private static final String USER = "root";
    private static final String PASSWORD = "Honey.8514";


    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


    public boolean addStudent(Student student) throws SQLException {

        String insertStudent = """
                INSERT INTO students
                (student_id, name, email, course_name,
                 programming_score, mathematics_score)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        String insertSkill = """
                INSERT INTO student_skills
                (student_id, skill_name)
                VALUES (?, ?)
                """;

        try (Connection connection = getConnection()) {

            connection.setAutoCommit(false);

            try {
                try (PreparedStatement studentStatement =
                             connection.prepareStatement(insertStudent)) {

                    studentStatement.setInt(1, student.getStudentId());
                    studentStatement.setString(2, student.getName());
                    studentStatement.setString(3, student.getEmail());
                    studentStatement.setString(4, student.getCourseName());
                    studentStatement.setInt(5, student.getProgrammingScore());
                    studentStatement.setInt(6, student.getMathematicsScore());

                    studentStatement.executeUpdate();
                }

                try (PreparedStatement skillStatement =
                             connection.prepareStatement(insertSkill)) {

                    for (String skill : student.getTechnicalSkills()) {

                        if (skill == null || skill.trim().isEmpty()) {
                            continue;
                        }

                        skillStatement.setInt(1, student.getStudentId());
                        skillStatement.setString(2, skill.trim());
                        skillStatement.addBatch();
                    }

                    skillStatement.executeBatch();
                }

                connection.commit();
                return true;

            } catch (SQLException exception) {
                connection.rollback();
                throw exception;
            }
        }
    }


    public List<Student> getAllStudents() throws SQLException {

        String sql = """
                SELECT s.student_id, s.name, s.email, s.course_name,
                       s.programming_score, s.mathematics_score,
                       GROUP_CONCAT(sk.skill_name ORDER BY sk.skill_name SEPARATOR ', ') AS skills
                FROM students s
                LEFT JOIN student_skills sk
                    ON s.student_id = sk.student_id
                GROUP BY s.student_id, s.name, s.email, s.course_name,
                         s.programming_score, s.mathematics_score
                ORDER BY s.student_id
                """;

        List<Student> students = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                students.add(mapStudent(resultSet));
            }
        }

        return students;
    }


    public Student getStudentById(int studentId) throws SQLException {

        String sql = """
                SELECT s.student_id, s.name, s.email, s.course_name,
                       s.programming_score, s.mathematics_score,
                       GROUP_CONCAT(sk.skill_name ORDER BY sk.skill_name SEPARATOR ', ') AS skills
                FROM students s
                LEFT JOIN student_skills sk
                    ON s.student_id = sk.student_id
                WHERE s.student_id = ?
                GROUP BY s.student_id, s.name, s.email, s.course_name,
                         s.programming_score, s.mathematics_score
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapStudent(resultSet);
                }
            }
        }

        return null;
    }


    public boolean updateStudentScoresAndSkills(Student student) throws SQLException {

        String updateStudent = """
                UPDATE students
                SET programming_score = ?,
                    mathematics_score = ?
                WHERE student_id = ?
                """;

        String deleteSkills = """
                DELETE FROM student_skills
                WHERE student_id = ?
                """;

        String insertSkill = """
                INSERT INTO student_skills
                (student_id, skill_name)
                VALUES (?, ?)
                """;

        try (Connection connection = getConnection()) {

            connection.setAutoCommit(false);

            try {
                try (PreparedStatement updateStatement =
                             connection.prepareStatement(updateStudent)) {

                    updateStatement.setInt(1, student.getProgrammingScore());
                    updateStatement.setInt(2, student.getMathematicsScore());
                    updateStatement.setInt(3, student.getStudentId());

                    int rowsUpdated = updateStatement.executeUpdate();

                    if (rowsUpdated == 0) {
                        connection.rollback();
                        return false;
                    }
                }

                try (PreparedStatement deleteStatement =
                             connection.prepareStatement(deleteSkills)) {

                    deleteStatement.setInt(1, student.getStudentId());
                    deleteStatement.executeUpdate();
                }

                try (PreparedStatement insertStatement =
                             connection.prepareStatement(insertSkill)) {

                    for (String skill : student.getTechnicalSkills()) {

                        if (skill == null || skill.trim().isEmpty()) {
                            continue;
                        }

                        insertStatement.setInt(1, student.getStudentId());
                        insertStatement.setString(2, skill.trim());
                        insertStatement.addBatch();
                    }

                    insertStatement.executeBatch();
                }

                connection.commit();
                return true;

            } catch (SQLException exception) {
                connection.rollback();
                throw exception;
            }
        }
    }


    public boolean deleteStudent(int studentId) throws SQLException {

        String deleteSkills = """
                DELETE FROM student_skills
                WHERE student_id = ?
                """;

        String deleteStudentSql = """
                DELETE FROM students
                WHERE student_id = ?
                """;

        try (Connection connection = getConnection()) {

            connection.setAutoCommit(false);

            try {
                try (PreparedStatement skillStatement =
                             connection.prepareStatement(deleteSkills)) {

                    skillStatement.setInt(1, studentId);
                    skillStatement.executeUpdate();
                }

                int rowsDeleted;

                try (PreparedStatement studentStatement =
                             connection.prepareStatement(deleteStudentSql)) {

                    studentStatement.setInt(1, studentId);
                    rowsDeleted = studentStatement.executeUpdate();
                }

                connection.commit();
                return rowsDeleted > 0;

            } catch (SQLException exception) {
                connection.rollback();
                throw exception;
            }
        }
    }



    public List<Student> getMeritList() throws SQLException {

        String sql = """
                SELECT s.student_id, s.name, s.email, s.course_name,
                       s.programming_score, s.mathematics_score,
                       GROUP_CONCAT(sk.skill_name ORDER BY sk.skill_name SEPARATOR ', ') AS skills,
                       (s.programming_score + s.mathematics_score) / 2.0 AS average_score
                FROM students s
                LEFT JOIN student_skills sk
                    ON s.student_id = sk.student_id
                GROUP BY s.student_id, s.name, s.email, s.course_name,
                         s.programming_score, s.mathematics_score
                ORDER BY average_score DESC
                """;

        List<Student> students = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                students.add(mapStudent(resultSet));
            }
        }

        return students;
    }


    private Student mapStudent(ResultSet resultSet) throws SQLException {

        List<String> skills = new ArrayList<>();

        String skillData = resultSet.getString("skills");

        if (skillData != null && !skillData.isEmpty()) {
            for (String skill : skillData.split(",")) {
                skills.add(skill.trim());
            }
        }

        return new Student(
                resultSet.getInt("student_id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("course_name"),
                resultSet.getInt("programming_score"),
                resultSet.getInt("mathematics_score"),
                skills
        );
    }
}