

package com.mit.scholarship.algorithm;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class ScholarshipCompatibilityAnalyzerTest {

    private final ScholarshipCompatibilityAnalyzer analyzer =
            new ScholarshipCompatibilityAnalyzer();

    @Test
    void shouldReturnCompatibleWhenRuleMatches() {

        CompatibilityRule rule = new CompatibilityRule(
                101L, 205L,
                ScholarshipCompatibilityAnalyzer.Status.COMPATIBLE,
                null, "Official source", null
        );

        assertEquals(
                ScholarshipCompatibilityAnalyzer.Status.COMPATIBLE,
                analyzer.analyze(101L, 205L, List.of(rule))
        );
    }

    @Test
    void shouldMatchScholarshipsInReverseOrder() {

        CompatibilityRule rule = new CompatibilityRule(
                101L, 205L,
                ScholarshipCompatibilityAnalyzer.Status.COMPATIBLE,
                null, "Official source", null
        );

        assertEquals(
                ScholarshipCompatibilityAnalyzer.Status.COMPATIBLE,
                analyzer.analyze(205L, 101L, List.of(rule))
        );
    }

    @Test
    void shouldRequireVerificationWhenRuleIsMissing() {

        assertEquals(
                ScholarshipCompatibilityAnalyzer.Status.NEEDS_VERIFICATION,
                analyzer.analyze(101L, 205L, List.of())
        );
    }

    @Test
    void shouldReturnConflictingWhenRuleConflicts() {

        CompatibilityRule rule = new CompatibilityRule(
                101L, 205L,
                ScholarshipCompatibilityAnalyzer.Status.CONFLICTING,
                "Both scholarships cannot be combined",
                "Official source", null
        );

        assertEquals(
                ScholarshipCompatibilityAnalyzer.Status.CONFLICTING,
                analyzer.analyze(101L, 205L, List.of(rule))
        );
    }
    
@Test
void shouldRequireVerificationWhenDuplicateRulesConflict() {

    CompatibilityRule rule1 = new CompatibilityRule(
            101L, 205L,
            ScholarshipCompatibilityAnalyzer.Status.COMPATIBLE,
            null, "Source A", null
    );

    CompatibilityRule rule2 = new CompatibilityRule(
            205L, 101L,
            ScholarshipCompatibilityAnalyzer.Status.CONFLICTING,
            "Restriction found",
            "Source B", null
    );

    assertEquals(
            ScholarshipCompatibilityAnalyzer.Status.NEEDS_VERIFICATION,
            analyzer.analyze(101L, 205L, List.of(rule1, rule2))
    );
}

@Test
void shouldRequireVerificationWhenSameScholarshipIsCompared() {

    assertEquals(
            ScholarshipCompatibilityAnalyzer.Status.NEEDS_VERIFICATION,
            analyzer.analyze(101L, 101L, List.of())
    );
}

@Test
void shouldClassifyCompatibleWhenBothAllowCombination() {
    assertEquals(
            ScholarshipCompatibilityAnalyzer.Status.COMPATIBLE,
            analyzer.classifyByCombinationFlag(true, true)
    );
}

@Test
void shouldClassifyConflictingWhenFirstDoesNotAllowCombination() {
    assertEquals(
            ScholarshipCompatibilityAnalyzer.Status.CONFLICTING,
            analyzer.classifyByCombinationFlag(false, true)
    );
}

@Test
void shouldClassifyConflictingWhenSecondDoesNotAllowCombination() {
    assertEquals(
            ScholarshipCompatibilityAnalyzer.Status.CONFLICTING,
            analyzer.classifyByCombinationFlag(true, false)
    );
}

@Test
void shouldRequireVerificationWhenCombinationFlagIsMissing() {
    assertEquals(
            ScholarshipCompatibilityAnalyzer.Status.NEEDS_VERIFICATION,
            analyzer.classifyByCombinationFlag(null, true)
    );
}


}
