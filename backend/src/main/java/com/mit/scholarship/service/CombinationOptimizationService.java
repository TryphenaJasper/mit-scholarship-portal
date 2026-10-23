
package com.mit.scholarship.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mit.scholarship.CompatibilityAnalysisService;
import com.mit.scholarship.algorithm.CombinationOptimizer;
import com.mit.scholarship.algorithm.CompatibilityRule;
import com.mit.scholarship.dto.StudentEligibilityProfile;
import com.mit.scholarship.model.Scholarship;
import com.mit.scholarship.repository.CompatibilityRuleRepository;

@Service
public class CombinationOptimizationService {

    private final ScholarshipService scholarshipService;
    private final StudentService studentService;
    private final CompatibilityAnalysisService compatibilityService;
    private final CompatibilityRuleRepository ruleRepository;
    private final CombinationOptimizer optimizer;

    public CombinationOptimizationService(
            ScholarshipService scholarshipService,
            StudentService studentService,
            CompatibilityAnalysisService compatibilityService,
            CompatibilityRuleRepository ruleRepository) {

        this.scholarshipService = scholarshipService;
        this.studentService = studentService;
        this.compatibilityService = compatibilityService;
        this.ruleRepository = ruleRepository;
        this.optimizer = new CombinationOptimizer();
    }

    public Map<String, Object> optimizeForStudent(Long studentId) {

        StudentEligibilityProfile profile =
                studentService.getEligibilityProfile(studentId);

        if (profile == null || profile.getStudent() == null
                || profile.getStudent().getSemester() == null) {
            throw new IllegalArgumentException(
                    "Student or current semester not found");
        }

        int currentSemester = profile.getStudent().getSemester();

        
List<Scholarship> eligibleScholarships =
        scholarshipService.getEligibleScholarships(studentId);

// ADD THESE LINES HERE
System.out.println("OPTIMIZE REQUEST studentId = " + studentId);
System.out.println("CURRENT SEMESTER = " + currentSemester);
System.out.println("ELIGIBLE SCHOLARSHIPS = " +
        eligibleScholarships.stream()
                .map(s -> s.getScholarshipId() + ":" + s.getName())
                .toList());

compatibilityService.analyzeAllPairs();


        List<CompatibilityRule> rules = ruleRepository.findAll();

        Map<String, String> compatibilityStatuses = new HashMap<>();

        for (CompatibilityRule rule : rules) {
            long id1 = Math.min(
                    rule.getScholarshipId1(),
                    rule.getScholarshipId2());
            long id2 = Math.max(
                    rule.getScholarshipId1(),
                    rule.getScholarshipId2());

            compatibilityStatuses.put(
                    id1 + "-" + id2,
                    rule.getRuleStatus() == null
                            ? null
                            : rule.getRuleStatus().name());
        }

        List<Long> eligibleIds = new ArrayList<>();
        Map<String, Double> scholarshipBenefits = new HashMap<>();
        Map<Long, Scholarship> scholarshipById = new HashMap<>();

        for (Scholarship scholarship : eligibleScholarships) {

            if (scholarship.getScholarshipId() == null
                    || scholarship.getAmount() == null
                    || scholarship.getAmountPeriod() == null) {
                continue;
            }

            double benefit;

            try {
                benefit = optimizer.calculateTotalBenefit(
                        scholarship.getAmount().doubleValue(),
                        scholarship.getAmountPeriod(),
                        currentSemester);
            } catch (IllegalArgumentException exception) {
                continue;
            }

            Long id = scholarship.getScholarshipId();

            eligibleIds.add(id);
            scholarshipBenefits.put(String.valueOf(id), benefit);
            scholarshipById.put(id, scholarship);
        }

        List<Long> bestCombination = optimizer.optimize(
                eligibleIds,
                compatibilityStatuses,
                scholarshipBenefits);

        double totalBenefit = 0.0;
        List<Map<String, Object>> selectedScholarships =
                new ArrayList<>();

        for (Long id : bestCombination) {
            Scholarship scholarship = scholarshipById.get(id);

            if (scholarship == null) {
                continue;
            }

            double benefit = scholarshipBenefits.get(
                    String.valueOf(id));

            totalBenefit += benefit;

            Map<String, Object> item = new LinkedHashMap<>();
            item.put("scholarshipId", id);
            item.put("name", scholarship.getName());
            item.put("amount", scholarship.getAmount());
            item.put("currency", scholarship.getCurrency());
            item.put("amountPeriod", scholarship.getAmountPeriod());
            item.put("estimatedRemainingBenefit", benefit);

            selectedScholarships.add(item);
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("studentId", studentId);
        response.put("currentSemester", currentSemester);
        response.put("eligibleScholarshipCount", eligibleIds.size());
        response.put("selectedScholarships", selectedScholarships);
        response.put("totalEstimatedBenefit", totalBenefit);

        return response;
    }
}