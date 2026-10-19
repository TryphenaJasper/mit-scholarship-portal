
package com.mit.scholarship.algorithm;

import java.time.LocalDate;

public class CompatibilityRule {

    private long scholarshipId1;
    private long scholarshipId2;

    private ScholarshipCompatibilityAnalyzer.Status ruleStatus;
    private String restrictionReason;
    private String sourceReference;
    private LocalDate lastVerifiedDate;

    public CompatibilityRule(
            long scholarshipId1,
            long scholarshipId2,
            ScholarshipCompatibilityAnalyzer.Status ruleStatus,
            String restrictionReason,
            String sourceReference,
            LocalDate lastVerifiedDate) {

        this.scholarshipId1 = scholarshipId1;
        this.scholarshipId2 = scholarshipId2;
        this.ruleStatus = ruleStatus;
        this.restrictionReason = restrictionReason;
        this.sourceReference = sourceReference;
        this.lastVerifiedDate = lastVerifiedDate;
    }

    public long getScholarshipId1() {
        return scholarshipId1;
    }

    public long getScholarshipId2() {
        return scholarshipId2;
    }

    public ScholarshipCompatibilityAnalyzer.Status getRuleStatus() {
        return ruleStatus;
    }

    public String getRestrictionReason() {
        return restrictionReason;
    }

    public String getSourceReference() {
        return sourceReference;
    }

    public LocalDate getLastVerifiedDate() {
        return lastVerifiedDate;
    }
}
