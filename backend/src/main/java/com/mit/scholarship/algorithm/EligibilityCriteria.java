package com.mit.scholarship.algorithm;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class EligibilityCriteria {

    public boolean evaluate(
            Object studentValue,
            String operator,
            String requiredValue) {

        if (studentValue == null) {
            return false;
        }

        switch (operator) {

            case "EQ":
                return equals(studentValue, requiredValue);

            case "NE":
                return !equals(studentValue, requiredValue);

            case "LT":
                return compare(studentValue, requiredValue) < 0;

            case "LTE":
                return compare(studentValue, requiredValue) <= 0;

            case "GT":
                return compare(studentValue, requiredValue) > 0;

            case "GTE":
                return compare(studentValue, requiredValue) >= 0;

            case "IN":
                return isIn(studentValue, requiredValue);

            case "NOT_IN":
                return !isIn(studentValue, requiredValue);

            default:
                throw new IllegalArgumentException(
                        "Unsupported operator: " + operator);
        }
    }

    private boolean equals(
            Object studentValue,
            String requiredValue) {

        return String.valueOf(studentValue)
                .equalsIgnoreCase(requiredValue);
    }

    private int compare(
            Object studentValue,
            String requiredValue) {

        BigDecimal studentNumber =
                new BigDecimal(String.valueOf(studentValue));

        BigDecimal requiredNumber =
                new BigDecimal(requiredValue);

        return studentNumber.compareTo(requiredNumber);
    }

    private boolean isIn(
            Object studentValue,
            String requiredValue) {

        List<String> allowedValues =
                Arrays.asList(requiredValue.split(","));

        String value =
                String.valueOf(studentValue).trim();

        return allowedValues.stream()
                .map(String::trim)
                .anyMatch(value::equalsIgnoreCase);
    }
}