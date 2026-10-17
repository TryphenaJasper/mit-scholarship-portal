
package com.mit.scholarship.service;

import com.mit.scholarship.dto.StudentRegistrationRequest;
import com.mit.scholarship.repository.StudentRepository;

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
}
