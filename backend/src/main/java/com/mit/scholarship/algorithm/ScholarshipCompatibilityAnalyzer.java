
package com.mit.scholarship.algorithm;

import java.util.List;

public class ScholarshipCompatibilityAnalyzer {

    public enum Status {
        COMPATIBLE,
        CONFLICTING,
        NEEDS_VERIFICATION
    }

    public CompatibilityRule findRule(
            long scholarshipId1,
            long scholarshipId2,
            List<CompatibilityRule> rules) {

        CompatibilityRule foundRule = null;

        for (CompatibilityRule rule : rules) {
            boolean directMatch =
                    rule.getScholarshipId1() == scholarshipId1
                    && rule.getScholarshipId2() == scholarshipId2;

            boolean reverseMatch =
                    rule.getScholarshipId1() == scholarshipId2
                    && rule.getScholarshipId2() == scholarshipId1;

            if (directMatch || reverseMatch) {
                if (foundRule != null) {
                    return null;
                }
                foundRule = rule;
            }
        }

        return foundRule;
    }

    public Status analyze(
            long scholarshipId1,
            long scholarshipId2,
            List<CompatibilityRule> rules) {

        if (scholarshipId1 == scholarshipId2) {
            return Status.NEEDS_VERIFICATION;
        }

        CompatibilityRule rule =
                findRule(scholarshipId1, scholarshipId2, rules);

        if (rule == null || rule.getRuleStatus() == null) {
            return Status.NEEDS_VERIFICATION;
        }

        return rule.getRuleStatus();
    }

    public Status classifyByCombinationFlag(
            Boolean allowsCombination1,
            Boolean allowsCombination2) {

        if (allowsCombination1 == null || allowsCombination2 == null) {
            return Status.NEEDS_VERIFICATION;
        }

        if (allowsCombination1 && allowsCombination2) {
            return Status.COMPATIBLE;
        }

        return Status.CONFLICTING;
    }
}