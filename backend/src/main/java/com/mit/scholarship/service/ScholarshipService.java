

package com.mit.scholarship.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mit.scholarship.algorithm.EligibilityCriteria;
import com.mit.scholarship.algorithm.StudentAttributeResolver;
import com.mit.scholarship.dto.StudentEligibilityProfile;
import com.mit.scholarship.model.Scholarship;
import com.mit.scholarship.model.ScholarshipRequirement;
import com.mit.scholarship.repository.ScholarshipRepository;
import com.mit.scholarship.repository.ScholarshipRequirementRepository;

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

    List<Scholarship> eligibleScholarships = new ArrayList<>();

    List<Scholarship> allScholarships =
            scholarshipRepository.findAll();

    System.out.println("Checking eligibility for studentId = " + studentId);

    for (Scholarship scholarship : allScholarships) {

        List<ScholarshipRequirement> requirements =
                requirementRepository.findByScholarshipId(
                        scholarship.getScholarshipId());

        System.out.println(
                "Scholarship ID: " + scholarship.getScholarshipId()
                + " | Requirements count: "
                + (requirements == null ? "null" : requirements.size())
        );

        // Do not assume eligibility when requirements are missing.
        if (requirements == null || requirements.isEmpty()) {
            System.out.println(
                    "SKIPPED scholarshipId="
                    + scholarship.getScholarshipId()
                    + " because no requirements were found."
            );
            continue;
        }

        boolean eligible = true;

      
for (ScholarshipRequirement requirement : requirements) {

    String attribute = requirement.getAttribute();

    /*
     * Scholarship 1 has separate income limits:
     * BC/MBC/SC/ST: below Rs. 250,000
     * SCC: below Rs. 200,000
     */
    if (Long.valueOf(1L).equals(
            scholarship.getScholarshipId())
            && "parental_annual_income"
                    .equalsIgnoreCase(attribute)) {

        Object communityValue =
                attributeResolver.resolve(profile, "community");

        if (communityValue == null) {
            System.out.println(
                    "REJECTED scholarshipId=1: community is missing."
            );
            eligible = false;
            break;
        }

        boolean isSCC = "SCC".equalsIgnoreCase(
                communityValue.toString().trim());

        String requiredValue =
                requirement.getRequiredValue();

        boolean isSCCLimit = "200000".equals(
                requiredValue == null
                        ? ""
                        : requiredValue.trim());

        // Skip the income limit that does not apply.
        if (isSCC != isSCCLimit) {
            continue;
        }
    }

    Object studentValue =
            attributeResolver.resolve(profile, attribute);

    // Skip other_scholarship only when the student value is NULL.
    if ("other_scholarship".equalsIgnoreCase(attribute)
            && studentValue == null) {

        System.out.println(
                "SKIPPED other_scholarship because student value is NULL"
        );

        continue;
    }

    boolean requirementPassed = false;

    if (studentValue != null) {
        requirementPassed = eligibilityCriteria.evaluate(
                studentValue,
                requirement.getOperator(),
                requirement.getRequiredValue());
    }

    if (requirement.isMandatory() && !requirementPassed) {

        System.out.println(
                "REJECTED scholarshipId="
                + scholarship.getScholarshipId()
                + ", attribute=" + attribute
                + ", studentValue=" + studentValue
                + ", operator=" + requirement.getOperator()
                + ", requiredValue="
                + requirement.getRequiredValue()
        );

        eligible = false;
        break;
    }
}


        System.out.println(
                "Scholarship ID: " + scholarship.getScholarshipId()
                + " | Eligible: " + eligible
        );

        if (eligible) {
            eligibleScholarships.add(scholarship);
        }
    }

    System.out.println("TOTAL ELIGIBLE SCHOLARSHIPS FOR STUDENT "+ studentId + " = " + eligibleScholarships.size());
    return eligibleScholarships;
}


    
}
