package com.mit.scholarship.algorithm;

public class ScholarshipCompatibilityAnalyzer {

    public enum Status {
        COMPATIBLE,
        CONFLICTING,
        NEEDS_VERIFICATION
    }

    public Status analyze(
            Status ruleStatus) {

        if (ruleStatus == null) {
            return Status.NEEDS_VERIFICATION;
        }

        return ruleStatus;
    }
}
