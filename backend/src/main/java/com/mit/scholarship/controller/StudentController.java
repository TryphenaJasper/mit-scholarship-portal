
package com.mit.scholarship.controller;

import com.mit.scholarship.dto.StudentRegistrationRequest;
import com.mit.scholarship.service.StudentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mit.scholarship.dto.StudentProfileResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
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
}

