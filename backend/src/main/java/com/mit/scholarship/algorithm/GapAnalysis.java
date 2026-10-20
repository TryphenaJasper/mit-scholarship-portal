
package com.mit.scholarship.algorithm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.mit.scholarship.dto.GapAnalysisResponse;
import com.mit.scholarship.dto.StudentEligibilityProfile;
import com.mit.scholarship.model.Scholarship;
import com.mit.scholarship.model.ScholarshipRequirement;

public class GapAnalysis {

    private final EligibilityCriteria eligibilityCriteria;
    private final StudentAttributeResolver attributeResolver;

    public GapAnalysis() {
        this.eligibilityCriteria = new EligibilityCriteria();
        this.attributeResolver = new StudentAttributeResolver();
    }

    public GapAnalysisResponse analyze(
            StudentEligibilityProfile profile,
            Scholarship scholarship,
            List<ScholarshipRequirement> requirements) {

        if (profile == null) {
            throw new IllegalArgumentException(
                    "Student profile cannot be null.");
        }

        if (scholarship == null) {
            throw new IllegalArgumentException(
                    "Scholarship cannot be null.");
        }

        GapAnalysisResponse response = new GapAnalysisResponse();

        response.setScholarshipId(scholarship.getScholarshipId());
        response.setScholarshipName(scholarship.getName());

        List<GapAnalysisResponse.RequirementGap> results =
                new ArrayList<>();

        int unmetCount = 0;
        int unknownCount = 0;
        boolean eligible = true;

        if (requirements != null) {
            for (ScholarshipRequirement requirement : requirements) {

                Object studentValue = attributeResolver.resolve(
                        profile, requirement.getAttribute());

                GapAnalysisResponse.RequirementGap result =
                        new GapAnalysisResponse.RequirementGap();

                result.setAttribute(requirement.getAttribute());
                result.setOperator(requirement.getOperator());
                result.setRequiredValue(requirement.getRequiredValue());
                result.setStudentValue(
                        studentValue == null
                                ? null
                                : String.valueOf(studentValue));
                result.setMandatory(requirement.isMandatory());

                String status;

                if (studentValue == null) {
                    status = "UNKNOWN";
                    unknownCount++;

                    result.setMessage(
                            "Student information is missing for this requirement.");

                } else {
                    try {
                        boolean passed = eligibilityCriteria.evaluate(
                                studentValue,
                                requirement.getOperator(),
                                requirement.getRequiredValue());

                        if (passed) {
                            status = "SATISFIED";
                            result.setMessage(
                                    "Requirement satisfied.");
                        } else {
                            status = "UNMET";
                            unmetCount++;

                            result.setMessage(
                                    buildFailureMessage(requirement));

                            result.setGap(calculateGap(
                                    studentValue,
                                    requirement.getRequiredValue(),
                                    requirement.getOperator()));
                        }

                    } catch (RuntimeException exception) {
                        status = "UNKNOWN";
                        unknownCount++;

                        result.setMessage(
                                "Unable to evaluate this requirement. "
                                + "Check its operator and configured values.");
                    }
                }

                result.setStatus(status);

                if (requirement.isMandatory()
                        && !"SATISFIED".equals(status)) {
                    eligible = false;
                }

                results.add(result);
            }
        }

        response.setRequirements(results);
        response.setUnmetCount(unmetCount);
        response.setUnknownCount(unknownCount);
        response.setEligible(eligible);

        return response;
    }

    private String buildFailureMessage(
            ScholarshipRequirement requirement) {

        String message = requirement.getFailureMessage();

        if (message != null && !message.trim().isEmpty()) {
            return message;
        }

        return "Requirement not satisfied: "
                + requirement.getAttribute()
                + " must satisfy "
                + requirement.getOperator()
                + " "
                + requirement.getRequiredValue()
                + ".";
    }

    private Double calculateGap(
            Object studentValue,
            String requiredValue,
            String operator) {

        if (studentValue == null || requiredValue == null) {
            return null;
        }

        try {
            BigDecimal current =
                    new BigDecimal(String.valueOf(studentValue));

            BigDecimal required =
                    new BigDecimal(requiredValue);

            BigDecimal difference;

            switch (operator) {
                case "GTE":
                    if (current.compareTo(required) < 0) {
                        difference = required.subtract(current);
                        return difference.doubleValue();
                    }
                    break;

                case "GT":
                    if (current.compareTo(required) <= 0) {
                        difference = required.subtract(current);
                        return difference.doubleValue();
                    }
                    break;

                case "LTE":
                    if (current.compareTo(required) > 0) {
                        difference = current.subtract(required);
                        return difference.doubleValue();
                    }
                    break;

                case "LT":
                    if (current.compareTo(required) >= 0) {
                        difference = current.subtract(required);
                        return difference.doubleValue();
                    }
                    break;

                default:
                    return null;
            }

        } catch (NumberFormatException exception) {
            return null;
        }

        return null;
    }
}
