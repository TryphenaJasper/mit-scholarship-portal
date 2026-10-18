
package com.mit.scholarship.algorithm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScholarshipCompatibilityAnalyzerTest {

    private final ScholarshipCompatibilityAnalyzer analyzer =
            new ScholarshipCompatibilityAnalyzer();

    @Test
    void shouldReturnCompatibleWhenRuleIsCompatible() {
        assertEquals(
                ScholarshipCompatibilityAnalyzer.Status.COMPATIBLE,
                analyzer.analyze(
                        ScholarshipCompatibilityAnalyzer.Status.COMPATIBLE)
        );
    }

    @Test
    void shouldReturnConflictingWhenRuleIsConflicting() {
        assertEquals(
                ScholarshipCompatibilityAnalyzer.Status.CONFLICTING,
                analyzer.analyze(
                        ScholarshipCompatibilityAnalyzer.Status.CONFLICTING)
        );
    }

    @Test
    void shouldRequireVerificationWhenRuleIsMissing() {
        assertEquals(
                ScholarshipCompatibilityAnalyzer.Status.NEEDS_VERIFICATION,
                analyzer.analyze(null)
        );
    }
}
