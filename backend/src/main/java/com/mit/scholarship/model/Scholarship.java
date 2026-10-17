
package com.mit.scholarship.model;

public class Scholarship {

    private String scholarshipId;
    private String name;
    private double minimumCgpa;
    private double maximumFamilyIncome;
    private String department;
    private int minimumYear;
    private int maximumYear;

    public Scholarship() {
    }

    public Scholarship(String scholarshipId, String name,
                       double minimumCgpa,
                       double maximumFamilyIncome,
                       String department,
                       int minimumYear, int maximumYear) {
        this.scholarshipId = scholarshipId;
        this.name = name;
        this.minimumCgpa = minimumCgpa;
        this.maximumFamilyIncome = maximumFamilyIncome;
        this.department = department;
        this.minimumYear = minimumYear;
        this.maximumYear = maximumYear;
    }

    public String getScholarshipId() {
        return scholarshipId;
    }

    public void setScholarshipId(String scholarshipId) {
        this.scholarshipId = scholarshipId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMinimumCgpa() {
        return minimumCgpa;
    }

    public void setMinimumCgpa(double minimumCgpa) {
        this.minimumCgpa = minimumCgpa;
    }

    public double getMaximumFamilyIncome() {
        return maximumFamilyIncome;
    }

    public void setMaximumFamilyIncome(double maximumFamilyIncome) {
        this.maximumFamilyIncome = maximumFamilyIncome;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getMinimumYear() {
        return minimumYear;
    }

    public void setMinimumYear(int minimumYear) {
        this.minimumYear = minimumYear;
    }

    public int getMaximumYear() {
        return maximumYear;
    }

    public void setMaximumYear(int maximumYear) {
        this.maximumYear = maximumYear;
    }
}

