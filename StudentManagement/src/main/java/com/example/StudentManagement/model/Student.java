package com.example.StudentManagement.model;
import java.util.ArrayList;
import java.util.List;

public class Student {

    private int studentId, programmingScore, mathematicsScore;
    private String name, email, courseName;
    private List<String> technicalSkills;

    public Student() {
        this.technicalSkills = new ArrayList<>();
    }

    public Student(int studentId, String name, String email, String courseName,int programmingScore, int mathematicsScore,List<String> technicalSkills) {
        this.studentId = studentId;
        setName(name);
        setEmail(email);
        setCourseName(courseName);
        setProgrammingScore(programmingScore);
        setMathematicsScore(mathematicsScore);

        if (technicalSkills == null) {
            this.technicalSkills = new ArrayList<>();
        } else {
            this.technicalSkills = new ArrayList<>(technicalSkills);
        }
    }
    public int getStudentId() {
        return studentId;
    }
    public void setStudentId(int id){
        studentId = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getProgrammingScore() {
        return programmingScore;
    }

    public void setProgrammingScore(int programmingScore) {
        this.programmingScore = programmingScore;
    }

    public int getMathematicsScore() {
        return mathematicsScore;
    }

    public void setMathematicsScore(int mathematicsScore) {
        this.mathematicsScore = mathematicsScore;
    }
    public List<String> getTechnicalSkills() {
        return new ArrayList<>(technicalSkills);
    }

    public void setTechnicalSkills(List<String> technicalSkills) {
        if (technicalSkills == null) {
            this.technicalSkills = new ArrayList<>();
        } else {
            this.technicalSkills = new ArrayList<>(technicalSkills);
        }
    }

    public void addTechnicalSkill(String skill) {
            technicalSkills.add(skill);
    }

    public void removeTechnicalSkill(String skill) {
            technicalSkills.remove(skill);
    }
}