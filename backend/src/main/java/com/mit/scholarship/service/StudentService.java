
package com.mit.scholarship.service;

import com.mit.scholarship.dto.StudentRegistrationRequest;
import com.mit.scholarship.repository.StudentRepository;
import com.mit.scholarship.dto.StudentProfileResponse;
import com.mit.scholarship.model.Student;
import com.mit.scholarship.dto.StudentProfilePatchRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.mit.scholarship.dto.StudentEligibilityProfile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    public Long registerStudent(StudentRegistrationRequest request) {

        if (request == null
                || request.getStudent() == null
                || request.getBackground() == null
                || request.getFinancial() == null
                || request.getEducation() == null) {

            throw new IllegalArgumentException(
                    "Student, background, financial and education details are required.");
        }

        // Step 1: Save basic student details and obtain the generated ID.
        Long studentId =
                studentRepository.saveStudent(request.getStudent());

        // Step 2: Save background details.
        studentRepository.saveBackground(
                studentId, request.getBackground());

        // Step 3: Save financial details.
        studentRepository.saveFinancial(
                studentId, request.getFinancial());

        // Step 4: Save education history.
        studentRepository.saveEducation(
                studentId, request.getEducation());

        // Step 5: Save semester-wise academic records.
        studentRepository.saveAcademicRecords(
                studentId, request.getAcademicRecords());

        return studentId;
    }
    public StudentProfileResponse getStudentProfile(Long studentId) {

    // 1. Get the basic student details first
    Student student = studentRepository.findStudentById(studentId);

    // 2. If the student doesn't exist, return null
    if (student == null) {
        return null;
    }

    // 3. Create the complete profile response
    StudentProfileResponse response = new StudentProfileResponse();

    response.setStudent(student);
    response.setBackground(
        studentRepository.findBackgroundByStudentId(studentId)
    );
    response.setFinancial(
        studentRepository.findFinancialByStudentId(studentId)
    );
    response.setEducation(
        studentRepository.findEducationByStudentId(studentId)
    );
    response.setAcademicRecords(
        studentRepository.findAcademicRecordsByStudentId(studentId)
    );

    return response;
    }
    @Transactional
public void patchStudentProfile(
        Long studentId,
        StudentProfilePatchRequest request) {

    if (request == null) {
        throw new IllegalArgumentException(
                "Patch request cannot be empty");
    }

    // Confirm that the student exists before updating anything.
    if (studentRepository.findStudentById(studentId) == null) {
        throw new IllegalArgumentException(
                "Student not found with ID: " + studentId);
    }

    boolean hasUpdates = false;

    JsonNode student = request.getStudent();
    JsonNode background = request.getBackground();
    JsonNode financial = request.getFinancial();
    JsonNode education = request.getEducation();
    JsonNode academicRecords = request.getAcademicRecords();

    if (student != null && !student.isNull()) {
        studentRepository.updateStudentFields(studentId, student);
        hasUpdates = true;
    }

    if (background != null && !background.isNull()) {
        studentRepository.updateBackgroundFields(studentId, background);
        hasUpdates = true;
    }

    if (financial != null && !financial.isNull()) {
        studentRepository.updateFinancialFields(studentId, financial);
        hasUpdates = true;
    }

    if (education != null && !education.isNull()) {
        studentRepository.updateEducationFields(studentId, education);
        hasUpdates = true;
    }

    if (academicRecords != null && !academicRecords.isNull()) {
        if (!academicRecords.isObject()) {
            throw new IllegalArgumentException(
                    "academicRecords must be a single JSON object");
        }

        studentRepository.updateAcademicFields(
                studentId, academicRecords);
        hasUpdates = true;
    }

    if (!hasUpdates) {
        throw new IllegalArgumentException(
                "Provide at least one section to update");
    }
}
public StudentEligibilityProfile getEligibilityProfile(Long studentId) {

    // Step 1: Fetch basic student details
    Student student = studentRepository.findStudentById(studentId);

    // Step 2: Stop if the student does not exist
    if (student == null) {
        return null;
    }

    // Step 3: Create the eligibility profile
    StudentEligibilityProfile profile = new StudentEligibilityProfile();

    // Step 4: Populate all five sections
    profile.setStudent(student);

    profile.setBackground(
            studentRepository.findBackgroundByStudentId(studentId));

    profile.setFinancial(
            studentRepository.findFinancialByStudentId(studentId));

    profile.setEducation(
            studentRepository.findEducationByStudentId(studentId));

    profile.setAcademicRecords(
            studentRepository.findAcademicRecordsByStudentId(studentId));

    // Step 5: Return the completed profile
    return profile;
}
}
