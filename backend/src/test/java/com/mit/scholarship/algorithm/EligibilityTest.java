
package com.mit.scholarship.algorithm;

import com.mit.scholarship.model.Scholarship;
import com.mit.scholarship.model.Student;

public class EligibilityTest {

    public static void main(String[] args) {

        EligibilityCriteria criteria = new EligibilityCriteria();

        // Sample student
        Student student = new Student(
                "S101",
                "CSE",
                2,
                8.5,
                200000
        );

        // Sample scholarship
        Scholarship scholarship = new Scholarship(
                "SCH101",
                "Merit Scholarship",
                7.5,
                300000,
                "CSE",
                1,
                3
        );

        // Test 1: Student meets all requirements
        System.out.println("Test 1: "
                + criteria.isEligible(student, scholarship));

        // Test 2: Student's CGPA is too low
        student.setCgpa(6.0);
        System.out.println("Test 2: "
                + criteria.isEligible(student, scholarship));

        // Test 3: Family income exceeds the limit
        student.setCgpa(8.5);
        student.setFamilyIncome(400000);
        System.out.println("Test 3: "
                + criteria.isEligible(student, scholarship));

        // Test 4: Department does not match
        student.setFamilyIncome(200000);
        student.setDepartment("ECE");
        System.out.println("Test 4: "
                + criteria.isEligible(student, scholarship));

        // Test 5: Year of study is outside the allowed range
        student.setDepartment("CSE");
        student.setYearOfStudy(4);
        System.out.println("Test 5: "
                + criteria.isEligible(student, scholarship));
    }
}

