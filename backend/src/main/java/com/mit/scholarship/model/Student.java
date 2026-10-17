
package com.mit.scholarship.model;

public class Student {

    private String studentId;
    private String department;
    private int yearOfStudy;
    private double cgpa;
    private double familyIncome;

    public Student() {
    }

    public Student(String studentId, String department,
                   int yearOfStudy, double cgpa,
                   double familyIncome) {
        this.studentId = studentId;
        this.department = department;
        this.yearOfStudy = yearOfStudy;
        this.cgpa = cgpa;
        this.familyIncome = familyIncome;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public double getCgpa() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }

    public double getFamilyIncome() {
        return familyIncome;
    }

    public void setFamilyIncome(double familyIncome) {
        this.familyIncome = familyIncome;
    }
}

