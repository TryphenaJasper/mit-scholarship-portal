
package com.mit.scholarship.service;

import com.mit.scholarship.algorithm.EligibilityCriteria;
import com.mit.scholarship.algorithm.StudentAttributeResolver;
import com.mit.scholarship.model.Scholarship;
import com.mit.scholarship.repository.ScholarshipRepository;
import com.mit.scholarship.repository.ScholarshipRequirementRepository;
import com.mit.scholarship.dto.StudentEligibilityProfile;
import com.mit.scholarship.model.ScholarshipRequirement;
import org.springframework.stereotype.Service;



import java.util.List;

@Service
public class ScholarshipService {

    private final ScholarshipRepository scholarshipRepository;
    private final ScholarshipRequirementRepository requirementRepository;
    private final StudentService studentService;
    private final EligibilityCriteria eligibilityCriteria;
    private final StudentAttributeResolver attributeResolver;

    public ScholarshipService(
        ScholarshipRepository scholarshipRepository,
        ScholarshipRequirementRepository requirementRepository,
        StudentService studentService) {

    this.scholarshipRepository = scholarshipRepository;
    this.requirementRepository = requirementRepository;
    this.studentService = studentService;
    this.eligibilityCriteria = new EligibilityCriteria();
    this.attributeResolver = new StudentAttributeResolver();
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

    List<Scholarship> eligibleScholarships = new java.util.ArrayList<>();

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

    return eligibleScholarships;
}
}

