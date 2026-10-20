
package com.mit.scholarship.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.mit.scholarship.CompatibilityAnalysisService;
import com.mit.scholarship.algorithm.CompatibilityRule;
import com.mit.scholarship.algorithm.ScholarshipCompatibilityAnalyzer;
import com.mit.scholarship.model.Scholarship;
import com.mit.scholarship.service.ScholarshipService;
@RestController
@RequestMapping("/api/scholarships")
public class ScholarshipController {

    private final ScholarshipService scholarshipService;
    private final CompatibilityAnalysisService compatibilityAnalysisService;
    private final com.mit.scholarship.service.CombinationOptimizationService combinationOptimizationService;

    public ScholarshipController(
        ScholarshipService scholarshipService,
        CompatibilityAnalysisService compatibilityAnalysisService,
        com.mit.scholarship.service.CombinationOptimizationService combinationOptimizationService) {

    this.scholarshipService = scholarshipService;
    this.compatibilityAnalysisService = compatibilityAnalysisService;
    this.combinationOptimizationService = combinationOptimizationService;
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

    @GetMapping("/compatibility/{id1}/{id2}")
    public ScholarshipCompatibilityAnalyzer.Status checkCompatibility(
            @PathVariable Long id1,
            @PathVariable Long id2) {

        return compatibilityAnalysisService.analyze(id1, id2);
    }

    @GetMapping("/compatibility/analyze-all")
    public List<CompatibilityRule> analyzeAllCompatibilityPairs() {
        return compatibilityAnalysisService.analyzeAllPairs();
    }
    @GetMapping("/optimize/{studentId}")
public java.util.Map<String, Object> optimizeCombination(
        @PathVariable Long studentId) {

    return combinationOptimizationService.optimizeForStudent(studentId);
}
}