
package com.mit.scholarship.dto;

import java.util.ArrayList;
import java.util.List;

public class GapAnalysisResponse {

    private Long scholarshipId;
    private String scholarshipName;
    private boolean eligible;
    private int unmetCount;
    private int unknownCount;

    private List<RequirementGap> requirements = new ArrayList<>();

    public Long getScholarshipId() {
        return scholarshipId;
    }

    public void setScholarshipId(Long scholarshipId) {
        this.scholarshipId = scholarshipId;
    }

    public String getScholarshipName() {
        return scholarshipName;
    }

    public void setScholarshipName(String scholarshipName) {
        this.scholarshipName = scholarshipName;
    }

    public boolean isEligible() {
        return eligible;
    }

    public void setEligible(boolean eligible) {
        this.eligible = eligible;
    }

    public int getUnmetCount() {
        return unmetCount;
    }

    public void setUnmetCount(int unmetCount) {
        this.unmetCount = unmetCount;
    }

    public int getUnknownCount() {
        return unknownCount;
    }

    public void setUnknownCount(int unknownCount) {
        this.unknownCount = unknownCount;
    }

    public List<RequirementGap> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<RequirementGap> requirements) {
        this.requirements = requirements;
    }

    public static class RequirementGap {

        private String attribute;
        private String operator;
        private String requiredValue;
        private String studentValue;
        private String status;
        private String message;
        private Double gap;
        private boolean mandatory;

        public String getAttribute() {
            return attribute;
        }

        public void setAttribute(String attribute) {
            this.attribute = attribute;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public String getRequiredValue() {
            return requiredValue;
        }

        public void setRequiredValue(String requiredValue) {
            this.requiredValue = requiredValue;
        }

        public String getStudentValue() {
            return studentValue;
        }

        public void setStudentValue(String studentValue) {
            this.studentValue = studentValue;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Double getGap() {
            return gap;
        }

        public void setGap(Double gap) {
            this.gap = gap;
        }

        public boolean isMandatory() {
            return mandatory;
        }

        public void setMandatory(boolean mandatory) {
            this.mandatory = mandatory;
        }
    }
}
