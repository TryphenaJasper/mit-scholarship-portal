
package com.mit.scholarship.algorithm;

import com.mit.scholarship.model.Student;
import com.mit.scholarship.model.Scholarship;

public class EligibilityCriteria {

    public boolean isEligible(Student student, Scholarship scholarship) {

        // Check minimum CGPA
        if (student.getCgpa() < scholarship.getMinimumCgpa()) {
            return false;
        }

        // Check maximum family income
        if (student.getFamilyIncome()
                > scholarship.getMaximumFamilyIncome()) {
            return false;
        }

        // Check department
        if (scholarship.getDepartment() != null
                && !scholarship.getDepartment().equalsIgnoreCase("ALL")
                && !scholarship.getDepartment()
                        .equalsIgnoreCase(student.getDepartment())) {
            return false;
        }

        // Check year of study
        if (student.getYearOfStudy() < scholarship.getMinimumYear()
                || student.getYearOfStudy() > scholarship.getMaximumYear()) {
            return false;
        }

        // All criteria passed
        return true;
    }
}

