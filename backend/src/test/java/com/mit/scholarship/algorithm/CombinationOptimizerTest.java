
package com.mit.scholarship.algorithm;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class CombinationOptimizerTest {

    private final CombinationOptimizer optimizer = new CombinationOptimizer();

    @Test
    void calculatesRemainingSemesters() {
        assertEquals(5, optimizer.calculateRemainingSemesters(3));
        assertEquals(0, optimizer.calculateRemainingSemesters(8));
    }

    @Test
    void calculatesTotalBenefit() 
    {
        assertEquals(6000.0,optimizer.calculateTotalBenefit(1000, "monthly", 7));
        assertEquals(10000.0,optimizer.calculateTotalBenefit(10000, "one-time", 3));
    }
    @Test
    void rejectsIncompatibleScholarships() {
        Map<String, String> compatibility = Map.of(
                "1-2", "COMPATIBLE",
                "1-3", "CONFLICTING",
                "2-3", "COMPATIBLE"
        );

        assertTrue(optimizer.areAllPairsCompatible(
                List.of(1L, 2L), compatibility));

        assertFalse(optimizer.areAllPairsCompatible(
                List.of(1L, 2L, 3L), compatibility));
    }
    
    @Test
    void selectsBestCombinationUsingSampleData() {
        List<Long> eligibleIds = List.of(1L, 2L, 3L);

        Map<String, String> compatibility = Map.of(
                "1-2", "COMPATIBLE",
                "1-3", "COMPATIBLE",
                "2-3", "INCOMPATIBLE"
        );

        Map<String, Double> benefits = Map.of(
                "1", 20000.0,
                "2", 30000.0,
                "3", 50000.0
        );

        List<Long> best = optimizer.optimize(
                eligibleIds, compatibility, benefits
        );

        assertEquals(List.of(1L, 3L), best);
        assertEquals(70000.0,
                best.stream()
                    .mapToDouble(id -> benefits.get(String.valueOf(id)))
                    .sum());
    }

    @Test
    void selectsSingleScholarshipWhenItHasHighestBenefit() {
        List<Long> eligibleIds = List.of(1L, 2L);

        Map<String, String> compatibility = Map.of(
                "1-2", "INCOMPATIBLE"
        );

        Map<String, Double> benefits = Map.of(
                "1", 15000.0,
                "2", 40000.0
        );

        List<Long> best = optimizer.optimize(
                eligibleIds, compatibility, benefits
        );

        assertEquals(List.of(2L), best);
    }
    @Test
void returnsEmptyCombinationWhenBenefitDataIsMissing() {
    List<Long> eligibleIds = List.of(1L, 2L);

    Map<String, String> compatibility = Map.of(
            "1-2", "COMPATIBLE"
    );

    Map<String, Double> benefits = Map.of(
            "1", 20000.0
    );

    List<Long> best = optimizer.optimize(
            eligibleIds, compatibility, benefits
    );

    assertEquals(List.of(1L), best);
}

}
