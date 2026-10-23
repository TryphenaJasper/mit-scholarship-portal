
package com.mit.scholarship.algorithm;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;

public class CombinationOptimizer {

    private static final int TOTAL_BE_SEMESTERS = 8;

    public int calculateRemainingSemesters(int currentSemester) {

        if (currentSemester < 1 ||
            currentSemester > TOTAL_BE_SEMESTERS) {

            throw new IllegalArgumentException(
                "Current semester must be between 1 and 8"
            );
        }

        return TOTAL_BE_SEMESTERS - currentSemester;
    }

    public double calculateRemainingYears(int currentSemester) {

        int remainingSemesters =
            calculateRemainingSemesters(currentSemester);

        return remainingSemesters / 2.0;
    }
    public double calculateTotalBenefit(
        double amount,
        String amountPeriod,
        int currentSemester) {

    double remainingYears =
            calculateRemainingYears(currentSemester);

    if (amount < 0) {
        throw new IllegalArgumentException(
                "Amount cannot be negative"
        );
    }

    if (amountPeriod == null) {
        throw new IllegalArgumentException(
                "Amount period must be specified"
        );
    }

    switch (amountPeriod.toLowerCase().trim()) {
        case "monthly":
            return amount * 12 * remainingYears;

        case "yearly":
            return amount * remainingYears;

        case "one-time":
            return amount;

        default:
            throw new IllegalArgumentException(
                    "Unsupported amount period: " + amountPeriod
            );
    }
}
public boolean areCompatible(String status) {
    return status != null
            && status.trim().equalsIgnoreCase("COMPATIBLE");
}
public boolean areAllPairsCompatible(
        List<Long> scholarshipIds,
        Map<String, String> compatibilityStatuses) {

    if (scholarshipIds == null ||
            compatibilityStatuses == null) {
        return false;
    }

    for (int i = 0; i < scholarshipIds.size(); i++) {
        for (int j = i + 1; j < scholarshipIds.size(); j++) {

            Long id1 = scholarshipIds.get(i);
            Long id2 = scholarshipIds.get(j);

            if (id1 == null || id2 == null || id1.equals(id2)) {
                return false;
            }

            long smallerId = Math.min(id1, id2);
            long largerId = Math.max(id1, id2);

            String key = smallerId + "-" + largerId;
            String status = compatibilityStatuses.get(key);

            if (!areCompatible(status)) {
                return false;
            }
        }
    }

    return true;
}
public List<Long> selectBestCombination(
        List<List<Long>> validCombinations,
        Map<String, Double> scholarshipBenefits) {

    List<Long> bestCombination = new ArrayList<>();
    double highestBenefit = -1;

    for (List<Long> combination : validCombinations) {

        double totalBenefit = 0;

        for (Long scholarshipId : combination) {
            Double benefit = scholarshipBenefits.get(
                    String.valueOf(scholarshipId)
            );

            if (benefit == null || benefit < 0) {
                totalBenefit = -1;
                break;
            }

            totalBenefit += benefit;
        }

        if (totalBenefit > highestBenefit) {
            highestBenefit = totalBenefit;
            bestCombination = new ArrayList<>(combination);
        }
    }

    return bestCombination;
}
public List<List<Long>> generateValidCombinations(
        List<Long> eligibleScholarshipIds,
        Map<String, String> compatibilityStatuses) {

    List<List<Long>> validCombinations = new ArrayList<>();

    if (eligibleScholarshipIds == null ||
            compatibilityStatuses == null) {
        return validCombinations;
    }

    int n = eligibleScholarshipIds.size();

    if (n >= 31) {
        throw new IllegalArgumentException(
                "Too many scholarships for this combination method"
        );
    }

    for (int mask = 1; mask < (1 << n); mask++) {
        List<Long> combination = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            if ((mask & (1 << i)) != 0) {
                combination.add(eligibleScholarshipIds.get(i));
            }
        }

        if (areAllPairsCompatible(
                combination, compatibilityStatuses)) {
            validCombinations.add(combination);
        }
    }

    return validCombinations;
}
public List<Long> optimize(
        List<Long> eligibleScholarshipIds,
        Map<String, String> compatibilityStatuses,
        Map<String, Double> scholarshipBenefits) {

    List<List<Long>> validCombinations =
            generateValidCombinations(
                    eligibleScholarshipIds,
                    compatibilityStatuses
            );

    return selectBestCombination(
            validCombinations,
            scholarshipBenefits
    );
}
}
