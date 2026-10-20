
package com.mit.scholarship.service;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mit.scholarship.algorithm.EligibilityCriteria;
import com.mit.scholarship.algorithm.GapAnalysis;
import com.mit.scholarship.algorithm.StudentAttributeResolver;
import com.mit.scholarship.dto.GapAnalysisResponse;
import com.mit.scholarship.dto.StudentEligibilityProfile;
import com.mit.scholarship.model.Scholarship;
import com.mit.scholarship.model.ScholarshipRequirement;
import com.mit.scholarship.repository.GapAnalysisResultRepository;
import com.mit.scholarship.repository.GapAnalysisRunRepository;
import com.mit.scholarship.repository.ScholarshipRepository;
import com.mit.scholarship.repository.ScholarshipRequirementRepository;

@Service
public class ScholarshipService {

    private final ScholarshipRepository scholarshipRepository;
    private final ScholarshipRequirementRepository requirementRepository;
    private final StudentService studentService;

    private final EligibilityCriteria eligibilityCriteria;
    private final StudentAttributeResolver attributeResolver;

    private final GapAnalysis gapAnalysis;
    private final GapAnalysisRunRepository gapAnalysisRunRepository;
    private final GapAnalysisResultRepository gapAnalysisResultRepository;

    public ScholarshipService(
        ScholarshipRepository scholarshipRepository,
        ScholarshipRequirementRepository requirementRepository,
        StudentService studentService,
        GapAnalysisRunRepository gapAnalysisRunRepository,
        GapAnalysisResultRepository gapAnalysisResultRepository) {

    this.scholarshipRepository = scholarshipRepository;
    this.requirementRepository = requirementRepository;
    this.studentService = studentService;

    this.gapAnalysisRunRepository = gapAnalysisRunRepository;
    this.gapAnalysisResultRepository = gapAnalysisResultRepository;

    this.eligibilityCriteria = new EligibilityCriteria();
    this.attributeResolver = new StudentAttributeResolver();
    this.gapAnalysis = new GapAnalysis();
}

    public List<Scholarship> getAllScholarships() {
        return scholarshipRepository.findAll();
    }

    public Scholarship getScholarshipById(Long id) {
        return scholarshipRepository.findById(id);
    }

    public List<Scholarship> getEligibleScholarships(Long studentId) {

        StudentEligibilityProfile profile =
                studentService.getEligibilityProfile(studentId);

        if (profile == null) {
            throw new IllegalArgumentException(
                    "Student not found with ID: " + studentId);
        }

        List<Scholarship> eligibleScholarships = new ArrayList<>();

        List<Scholarship> allScholarships =
                scholarshipRepository.findAll();

        for (Scholarship scholarship : allScholarships) {

            List<ScholarshipRequirement> requirements =
                    requirementRepository.findByScholarshipId(
                            scholarship.getScholarshipId());

            boolean eligible = true;

            for (ScholarshipRequirement requirement : requirements) {

                Object studentValue = attributeResolver.resolve(
                        profile,
                        requirement.getAttribute());

                boolean requirementPassed = false;

                if (studentValue != null) {
                    requirementPassed = eligibilityCriteria.evaluate(
                            studentValue,
                            requirement.getOperator(),
                            requirement.getRequiredValue());
                }

                if (requirement.isMandatory() && !requirementPassed) {
                    eligible = false;
                    break;
                }
            }

            if (eligible) {
                eligibleScholarships.add(scholarship);
            }
        }

        return eligibleScholarships;}
@Transactional
public GapAnalysisResponse getScholarshipGapAnalysis(
        Long studentId,
        Long scholarshipId) {

    StudentEligibilityProfile profile =
            studentService.getEligibilityProfile(studentId);

    if (profile == null) {
        throw new IllegalArgumentException(
                "Student not found with ID: " + studentId);
    }

    Scholarship scholarship =
            scholarshipRepository.findById(scholarshipId);

    if (scholarship == null) {
        throw new IllegalArgumentException(
                "Scholarship not found with ID: " + scholarshipId);
    }

    List<ScholarshipRequirement> requirements =
            requirementRepository.findByScholarshipId(scholarshipId);

    // 1. Run your existing algorithm
    GapAnalysisResponse response = gapAnalysis.analyze(
            profile, scholarship, requirements);

    // 2. Save the overall analysis and obtain its generated ID
    Long analysisId = gapAnalysisRunRepository.saveRun(
            studentId,
            scholarshipId,
            response.isEligible(),
            response.getUnmetCount(),
            response.getUnknownCount()
    );

    // 3. Save all individual requirement results
    gapAnalysisResultRepository.saveResults(
            analysisId,
            response.getRequirements()
    );

    // 4. Return the same response to the frontend
    return response;
}

}
