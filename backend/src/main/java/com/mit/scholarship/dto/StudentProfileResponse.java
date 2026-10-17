
package com.mit.scholarship.dto;

import com.mit.scholarship.model.Student;
import com.mit.scholarship.dto.StudentRegistrationRequest.BackgroundDetails;
import com.mit.scholarship.dto.StudentRegistrationRequest.FinancialDetails;
import com.mit.scholarship.dto.StudentRegistrationRequest.EducationDetails;
import com.mit.scholarship.dto.StudentRegistrationRequest.AcademicRecordDetails;

import java.util.List;

public class StudentProfileResponse {

    private Student student;
    private BackgroundDetails background;
    private FinancialDetails financial;
    private EducationDetails education;
    private List<AcademicRecordDetails> academicRecords;

    public StudentProfileResponse() {
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public BackgroundDetails getBackground() {
        return background;
    }

    public void setBackground(BackgroundDetails background) {
        this.background = background;
    }

    public FinancialDetails getFinancial() {
        return financial;
    }

    public void setFinancial(FinancialDetails financial) {
        this.financial = financial;
    }

    public EducationDetails getEducation() {
        return education;
    }

    public void setEducation(EducationDetails education) {
        this.education = education;
    }

    public List<AcademicRecordDetails> getAcademicRecords() {
        return academicRecords;
    }

    public void setAcademicRecords(
            List<AcademicRecordDetails> academicRecords) {
        this.academicRecords = academicRecords;
    }
}
