
package com.mit.scholarship.algorithm;

import com.mit.scholarship.dto.StudentEligibilityProfile;
import com.mit.scholarship.dto.StudentRegistrationRequest.AcademicRecordDetails;

public class StudentAttributeResolver {

    public Object resolve(
            StudentEligibilityProfile profile,
            String attribute) {

        if (profile == null || attribute == null) {
            return null;
        }

        switch (attribute.toLowerCase()) {

            // Basic student details
            case "gender":
                return profile.getStudent() == null
                        ? null : profile.getStudent().getGender();

            case "degree":
                return profile.getStudent() == null
                        ? null : profile.getStudent().getDegree();
            case "degree_type": {
    if (profile.getStudent() == null
            || profile.getStudent().getDegree() == null) {
        return null;
    }

    String degree = profile.getStudent().getDegree()
            .trim().toUpperCase();

    if (degree.equals("BE")
            || degree.equals("B.E.")
            || degree.equals("BTECH")
            || degree.equals("B.TECH")
            || degree.equals("UG")) {
        return "UG";
    }

    if (degree.equals("ME")
            || degree.equals("M.E.")
            || degree.equals("MTECH")
            || degree.equals("M.TECH")
            || degree.equals("PG")) {
        return "PG";
    }

    return degree;
}

            case "department":
                return profile.getStudent() == null
                        ? null : profile.getStudent().getDepartment();

            case "year_of_study":
                return profile.getStudent() == null
                        ? null : profile.getStudent().getYearOfStudy();

            // Background details
            case "community":
                return profile.getBackground() == null
                        ? null : profile.getBackground().getCommunity();

            case "religion":
                return profile.getBackground() == null
                        ? null : profile.getBackground().getReligion();

            case "is_first_graduate":
                return profile.getBackground() == null
                        ? null : profile.getBackground().getIsFirstGraduate();

            case "is_differently_abled":
                return profile.getBackground() == null
                        ? null : profile.getBackground().getIsDifferentlyAbled();

            case "is_orphan":
                return profile.getBackground() == null
                        ? null : profile.getBackground().getIsOrphan();

            case "has_single_parent":
                return profile.getBackground() == null
                        ? null : profile.getBackground().getHasSingleParent();

            case "admission_mode":
                return profile.getBackground() == null
                        ? null : profile.getBackground().getAdmissionMode();
            
case "other_scholarship":
case "has_other_scholarship":
    return profile.getBackground() == null
            ? null
            : profile.getBackground().getHasOtherScholarship();

            case "single_window_admission": {
    if (profile.getBackground() == null
            || profile.getBackground().getAdmissionMode() == null) {
        return null;
    }

    String mode = profile.getBackground().getAdmissionMode()
            .trim()
            .replace('-', '_')
            .replace(' ', '_')
            .toUpperCase();

    if (mode.equals("SINGLE_WINDOW")) {
        return true;
    }

    // Other admission modes need verification.
    return null;
}

            case "is_beedi_cine_worker_dependent":
                return profile.getBackground() == null
                        ? null : profile.getBackground()
                                  .getIsBeediCineWorkerDependent();

            case "is_capf_assam_rifles_dependent":
                return profile.getBackground() == null
                        ? null : profile.getBackground()
                                  .getIsCapfAssamRiflesDependent();

            // Financial details
            case "parental_annual_income":
                return profile.getFinancial() == null
                        ? null : profile.getFinancial().getParentalAnnualIncome();

            case "parental_monthly_income":
                return profile.getFinancial() == null
                        ? null : profile.getFinancial().getParentalMonthlyIncome();

            case "income_certificate_available":
                return profile.getFinancial() == null
                        ? null : profile.getFinancial().getIncomeCertificateAvailable();

            // Education details
            case "class_10_institution_type":
                return profile.getEducation() == null
                        ? null : profile.getEducation().getClass10InstitutionType();

            case "class_12_institution_type":
                return profile.getEducation() == null
                        ? null : profile.getEducation().getClass12InstitutionType();

            case "studied_gov_class_6_to_12":
                return profile.getEducation() == null
                        ? null : profile.getEducation().getStudiedGovClass6To12();

            case "studied_gov_class_10_to_12":
                return profile.getEducation() == null
                        ? null : profile.getEducation().getStudiedGovClass10To12();

            case "class_10_percentage":
                return profile.getEducation() == null
                        ? null : profile.getEducation().getClass10Percentage();

            case "class_12_percentage":
                return profile.getEducation() == null
                        ? null : profile.getEducation().getClass12Percentage();

            case "class_12_completion_year":
                return profile.getEducation() == null
                        ? null : profile.getEducation().getClass12CompletionYear();
            // Academic details: use the latest semester's record
case "cgpa":
case "attendance_percentage":
case "arrears_count":
case "conduct_good":

    if (profile.getAcademicRecords() == null
            || profile.getAcademicRecords().isEmpty()) {
        return null;
    }

    AcademicRecordDetails latestRecord =
            profile.getAcademicRecords().get(
                    profile.getAcademicRecords().size() - 1);

    switch (attribute.toLowerCase()) {
        case "cgpa":
            return latestRecord.getCgpa();

        case "attendance_percentage":
            return latestRecord.getAttendancePercentage();

        case "arrears_count":
            return latestRecord.getArrearsCount();

        case "conduct_good":
            return latestRecord.getConductGood();

        default:
            return null;
    }
            default:
                // Unsupported or not-yet-implemented attributes
                return null;
        }
    }
}
