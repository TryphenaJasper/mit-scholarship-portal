package com.mit.scholarship.controller;

import com.mit.scholarship.model.Scholarship;
import com.mit.scholarship.service.ScholarshipService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/scholarships")
public class ScholarshipController {

    private final ScholarshipService scholarshipService;

    public ScholarshipController(
            ScholarshipService scholarshipService) {
        this.scholarshipService = scholarshipService;
    }

    @GetMapping
    public List<Scholarship> getAllScholarships() {
        return scholarshipService.getAllScholarships();
    }
    @GetMapping("/{id}")
    public Scholarship getScholarshipById(@PathVariable Long id) {

        Scholarship scholarship = scholarshipService.getScholarshipById(id);

        if (scholarship == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Scholarship not found with ID: " + id
            );
        }

    return scholarship;
    }
}
