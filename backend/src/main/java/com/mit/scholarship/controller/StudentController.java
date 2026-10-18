
package com.mit.scholarship.controller;

import com.mit.scholarship.dto.StudentRegistrationRequest;
import com.mit.scholarship.service.StudentService;
import com.mit.scholarship.dto.StudentProfilePatchRequest;

import com.mit.scholarship.model.Scholarship;
import com.mit.scholarship.service.ScholarshipService;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mit.scholarship.dto.StudentProfileResponse;

import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;
    private final ScholarshipService scholarshipService;

    public StudentController(
        StudentService studentService,
        ScholarshipService scholarshipService) {
    this.studentService = studentService;
    this.scholarshipService = scholarshipService;
}

    @PostMapping
    public ResponseEntity<Map<String, Object>> registerStudent(
            @RequestBody StudentRegistrationRequest request) {

        Long studentId = studentService.registerStudent(request);

        Map<String, Object> response = Map.of(
                "message", "Student registered successfully",
                "studentId", studentId
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<StudentProfileResponse> getStudentProfile(
        @PathVariable Long id) {

        StudentProfileResponse profile =
            studentService.getStudentProfile(id);

        if (profile == null) {
        return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(profile);
    }
    @PatchMapping("/{id}")
public ResponseEntity<?> patchStudentProfile(
        @PathVariable Long id,
        @RequestBody StudentProfilePatchRequest request) {

    try {
        studentService.patchStudentProfile(id, request);

        return ResponseEntity.ok(
                java.util.Map.of(
                        "message", "Student profile updated successfully",
                        "studentId", id
                )
        );

    } catch (IllegalArgumentException e) {
        if (e.getMessage() != null
                && e.getMessage().startsWith("Student not found")) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.badRequest().body(
                java.util.Map.of("error", e.getMessage())
        );
    }
}
@GetMapping("/{id}/eligibility")
public ResponseEntity<?> getEligibleScholarships(
        @PathVariable Long id) {

    try {
        List<Scholarship> scholarships =
                scholarshipService.getEligibleScholarships(id);

        return ResponseEntity.ok(scholarships);

    } catch (IllegalArgumentException e) {
        if (e.getMessage() != null
                && e.getMessage().startsWith("Student not found")) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.badRequest().body(
                java.util.Map.of("error", e.getMessage()));
    }
}
}

