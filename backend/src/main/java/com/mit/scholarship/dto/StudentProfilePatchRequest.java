
package com.mit.scholarship.dto;

import com.fasterxml.jackson.databind.JsonNode;

public class StudentProfilePatchRequest {

    private JsonNode student;
    private JsonNode background;
    private JsonNode financial;
    private JsonNode education;
    private JsonNode academicRecords;

    public StudentProfilePatchRequest() {
    }

    public JsonNode getStudent() {
        return student;
    }

    public void setStudent(JsonNode student) {
        this.student = student;
    }

    public JsonNode getBackground() {
        return background;
    }

    public void setBackground(JsonNode background) {
        this.background = background;
    }

    public JsonNode getFinancial() {
        return financial;
    }

    public void setFinancial(JsonNode financial) {
        this.financial = financial;
    }

    public JsonNode getEducation() {
        return education;
    }

    public void setEducation(JsonNode education) {
        this.education = education;
    }

    public JsonNode getAcademicRecords() {
        return academicRecords;
    }

    public void setAcademicRecords(JsonNode academicRecords) {
        this.academicRecords = academicRecords;
    }
}
