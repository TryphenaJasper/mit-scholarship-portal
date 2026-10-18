package com.mit.scholarship.algorithm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EligibilityTest {

    private final EligibilityCriteria criteria =
            new EligibilityCriteria();

    @Test
    void testEqual() {
        assertTrue(criteria.evaluate("FEMALE", "EQ", "female"));
    }

    @Test
    void testNotEqual() {
        assertTrue(criteria.evaluate("MALE", "NE", "FEMALE"));
    }

    @Test
    void testLessThan() {
        assertTrue(criteria.evaluate(180000, "LT", "250000"));
        assertFalse(criteria.evaluate(250000, "LT", "250000"));
    }

    @Test
    void testLessThanOrEqual() {
        assertTrue(criteria.evaluate(250000, "LTE", "250000"));
        assertFalse(criteria.evaluate(260000, "LTE", "250000"));
    }

    @Test
    void testGreaterThan() {
        assertTrue(criteria.evaluate(9, "GT", "8"));
        assertFalse(criteria.evaluate(8, "GT", "8"));
    }

    @Test
    void testGreaterThanOrEqual() {
        assertTrue(criteria.evaluate(7, "GTE", "7"));
        assertFalse(criteria.evaluate(6, "GTE", "7"));
    }

    @Test
    void testIn() {
        assertTrue(criteria.evaluate("BC", "IN", "BC,MBC,SC,ST"));
        assertFalse(criteria.evaluate("GENERAL", "IN", "BC,MBC,SC,ST"));
    }

    @Test
    void testNotIn() {
        assertTrue(criteria.evaluate("GENERAL", "NOT_IN", "BC,MBC,SC,ST"));
        assertFalse(criteria.evaluate("SC", "NOT_IN", "BC,MBC,SC,ST"));
    }

    @Test
    void testNullStudentValue() {
        assertFalse(criteria.evaluate(null, "EQ", "FEMALE"));
    }

    @Test
    void testUnsupportedOperator() {
        assertThrows(
                IllegalArgumentException.class,
                () -> criteria.evaluate("BC", "UNKNOWN", "BC")
        );
    }
}
