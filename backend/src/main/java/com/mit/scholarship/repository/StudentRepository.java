
package com.mit.scholarship.repository;

import com.mit.scholarship.dto.StudentRegistrationRequest;
import com.mit.scholarship.dto.StudentRegistrationRequest.AcademicRecordDetails;
import com.mit.scholarship.dto.StudentRegistrationRequest.BackgroundDetails;
import com.mit.scholarship.dto.StudentRegistrationRequest.EducationDetails;
import com.mit.scholarship.dto.StudentRegistrationRequest.FinancialDetails;
import com.mit.scholarship.model.Student;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

@Repository
public class StudentRepository {

    private final JdbcTemplate jdbcTemplate;

    public StudentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 1. Insert basic student details and return student_id
    public Long saveStudent(Student student) {

        String sql = """
                INSERT INTO students
                (register_no, name, email, gender, date_of_birth,
                 department, degree, year_of_study, semester)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, student.getRegisterNo());
            statement.setString(2, student.getName());
            statement.setString(3, student.getEmail());
            statement.setString(4, student.getGender());

            if (student.getDateOfBirth() != null) {
                statement.setDate(
                        5, java.sql.Date.valueOf(student.getDateOfBirth()));
            } else {
                statement.setNull(5, Types.DATE);
            }

            statement.setString(6, student.getDepartment());
            statement.setString(7, student.getDegree());
            statement.setObject(8, student.getYearOfStudy());
            statement.setObject(9, student.getSemester());

            return statement;
        }, keyHolder);

        if (keyHolder.getKey() == null) {
            throw new IllegalStateException(
                    "Failed to retrieve the generated student ID.");
        }

        return keyHolder.getKey().longValue();
    }

    // 2. Insert background details
    public void saveBackground(
            Long studentId, BackgroundDetails background) {

        String sql = """
                INSERT INTO student_background
                (student_id, community, religion, is_first_graduate,
                 is_differently_abled, is_orphan, has_single_parent,
                 admission_mode, is_beedi_cine_worker_dependent,
                 is_capf_assam_rifles_dependent)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.update(sql,
                studentId,
                background.getCommunity(),
                background.getReligion(),
                background.getIsFirstGraduate(),
                background.getIsDifferentlyAbled(),
                background.getIsOrphan(),
                background.getHasSingleParent(),
                background.getAdmissionMode(),
                background.getIsBeediCineWorkerDependent(),
                background.getIsCapfAssamRiflesDependent());
    }

    // 3. Insert financial details
    public void saveFinancial(
            Long studentId, FinancialDetails financial) {

        String sql = """
                INSERT INTO student_financial
                (student_id, parental_annual_income,
                 parental_monthly_income, income_certificate_available)
                VALUES (?, ?, ?, ?)
                """;

        jdbcTemplate.update(sql,
                studentId,
                financial.getParentalAnnualIncome(),
                financial.getParentalMonthlyIncome(),
                financial.getIncomeCertificateAvailable());
    }

    // 4. Insert school and education history
    public void saveEducation(
            Long studentId, EducationDetails education) {

        String sql = """
                INSERT INTO student_education_history
                (student_id, class_10_institution_type,
                 class_12_institution_type, studied_gov_class_6_to_12,
                 studied_gov_class_10_to_12, class_10_percentage,
                 class_12_percentage, class_12_completion_year)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        Boolean studiedGov6To12 =
                education.getStudiedGovClass6To12();

        Boolean studiedGov10To12 =
                education.getStudiedGovClass10To12();

        // Studying in a government school from classes 6–12
        // necessarily means studying in classes 10–12 as well.
        if (Boolean.TRUE.equals(studiedGov6To12)) {
            studiedGov10To12 = true;
        }

        jdbcTemplate.update(sql,
                studentId,
                education.getClass10InstitutionType(),
                education.getClass12InstitutionType(),
                studiedGov6To12,
                studiedGov10To12,
                education.getClass10Percentage(),
                education.getClass12Percentage(),
                education.getClass12CompletionYear());
    }

    // 5. Insert all supplied semester-wise academic records
    public void saveAcademicRecords(
            Long studentId, List<AcademicRecordDetails> records) {

        if (records == null || records.isEmpty()) {
            return;
        }

        String sql = """
                INSERT INTO student_academic_records
                (student_id, semester, cgpa, attendance_percentage,
                 arrears_count, conduct_good, academic_year)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        for (AcademicRecordDetails record : records) {
            jdbcTemplate.update(sql,
                    studentId,
                    record.getSemester(),
                    record.getCgpa(),
                    record.getAttendancePercentage(),
                    record.getArrearsCount(),
                    record.getConductGood(),
                    record.getAcademicYear());
        }
    }
}
