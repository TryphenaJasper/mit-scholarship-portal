
package com.mit.scholarship.controller;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mit.scholarship.dto.GapAnalysisResponse;
import com.mit.scholarship.dto.StudentProfilePatchRequest;
import com.mit.scholarship.dto.StudentProfileResponse;
import com.mit.scholarship.dto.StudentRegistrationRequest;
import com.mit.scholarship.model.Scholarship;
import com.mit.scholarship.service.ScholarshipService;
import com.mit.scholarship.service.StudentService;

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

@GetMapping("/{studentId}/scholarships/{scholarshipId}/gap-analysis")
public ResponseEntity<?> getScholarshipGapAnalysis(
        @PathVariable Long studentId,
        @PathVariable Long scholarshipId) {

    try {
        GapAnalysisResponse response =
                scholarshipService.getScholarshipGapAnalysis(
                        studentId, scholarshipId);

        return ResponseEntity.ok(response);

    } catch (IllegalArgumentException e) {

        if (e.getMessage() != null
                && (e.getMessage().startsWith("Student not found")
                || e.getMessage().startsWith("Scholarship not found"))) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.badRequest().body(
                java.util.Map.of("error", e.getMessage()));
    }
}

}

