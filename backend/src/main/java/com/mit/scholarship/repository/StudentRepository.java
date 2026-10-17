
package com.mit.scholarship.repository;

import com.mit.scholarship.dto.StudentRegistrationRequest;
import com.mit.scholarship.dto.StudentRegistrationRequest.AcademicRecordDetails;
import com.mit.scholarship.dto.StudentRegistrationRequest.BackgroundDetails;
import com.mit.scholarship.dto.StudentRegistrationRequest.EducationDetails;
import com.mit.scholarship.dto.StudentRegistrationRequest.FinancialDetails;
import com.mit.scholarship.model.Student;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    
    // 1. Retrieve basic student details
    public Student findStudentById(Long studentId) {

        String sql = """
                SELECT student_id, register_no, name, email, gender,
                       date_of_birth, department, degree,
                       year_of_study, semester
                FROM students
                WHERE student_id = ?
                """;

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Student student = new Student();

                student.setStudentId(rs.getLong("student_id"));
                student.setRegisterNo(rs.getString("register_no"));
                student.setName(rs.getString("name"));
                student.setEmail(rs.getString("email"));
                student.setGender(rs.getString("gender"));

                java.sql.Date dob = rs.getDate("date_of_birth");
                if (dob != null) {
                    student.setDateOfBirth(dob.toLocalDate());
                }

                student.setDepartment(rs.getString("department"));
                student.setDegree(rs.getString("degree"));
                student.setYearOfStudy(rs.getInt("year_of_study"));
                student.setSemester(rs.getInt("semester"));

                return student;
            }, studentId);

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // 2. Retrieve background details
    public StudentRegistrationRequest.BackgroundDetails
            findBackgroundByStudentId(Long studentId) {

        String sql = """
                SELECT community, religion, is_first_graduate,
                       is_differently_abled, is_orphan, has_single_parent,
                       admission_mode, is_beedi_cine_worker_dependent,
                       is_capf_assam_rifles_dependent
                FROM student_background
                WHERE student_id = ?
                """;

        List<StudentRegistrationRequest.BackgroundDetails> results =
                jdbcTemplate.query(sql, (rs, rowNum) -> {
                    StudentRegistrationRequest.BackgroundDetails details =
                            new StudentRegistrationRequest.BackgroundDetails();

                    details.setCommunity(rs.getString("community"));
                    details.setReligion(rs.getString("religion"));
                    details.setIsFirstGraduate(
                            getNullableBoolean(rs, "is_first_graduate"));
                    details.setIsDifferentlyAbled(
                            getNullableBoolean(rs, "is_differently_abled"));
                    details.setIsOrphan(
                            getNullableBoolean(rs, "is_orphan"));
                    details.setHasSingleParent(
                            getNullableBoolean(rs, "has_single_parent"));
                    details.setAdmissionMode(rs.getString("admission_mode"));
                    details.setIsBeediCineWorkerDependent(
                            getNullableBoolean(
                                    rs, "is_beedi_cine_worker_dependent"));
                    details.setIsCapfAssamRiflesDependent(
                            getNullableBoolean(
                                    rs, "is_capf_assam_rifles_dependent"));

                    return details;
                }, studentId);

        return results.isEmpty() ? null : results.get(0);
    }

    // 3. Retrieve financial details
    public StudentRegistrationRequest.FinancialDetails
            findFinancialByStudentId(Long studentId) {

        String sql = """
                SELECT parental_annual_income, parental_monthly_income,
                       income_certificate_available
                FROM student_financial
                WHERE student_id = ?
                """;

        List<StudentRegistrationRequest.FinancialDetails> results =
                jdbcTemplate.query(sql, (rs, rowNum) -> {
                    StudentRegistrationRequest.FinancialDetails details =
                            new StudentRegistrationRequest.FinancialDetails();

                    details.setParentalAnnualIncome(
                            rs.getBigDecimal("parental_annual_income"));
                    details.setParentalMonthlyIncome(
                            rs.getBigDecimal("parental_monthly_income"));
                    details.setIncomeCertificateAvailable(
                            getNullableBoolean(
                                    rs, "income_certificate_available"));

                    return details;
                }, studentId);

        return results.isEmpty() ? null : results.get(0);
    }

    // 4. Retrieve education details
    public StudentRegistrationRequest.EducationDetails
            findEducationByStudentId(Long studentId) {

        String sql = """
                SELECT class_10_institution_type, class_12_institution_type,
                       studied_gov_class_6_to_12,
                       studied_gov_class_10_to_12,
                       class_10_percentage, class_12_percentage,
                       class_12_completion_year
                FROM student_education_history
                WHERE student_id = ?
                ORDER BY education_id DESC
                LIMIT 1
                """;

        List<StudentRegistrationRequest.EducationDetails> results =
                jdbcTemplate.query(sql, (rs, rowNum) -> {
                    StudentRegistrationRequest.EducationDetails details =
                            new StudentRegistrationRequest.EducationDetails();

                    details.setClass10InstitutionType(
                            rs.getString("class_10_institution_type"));
                    details.setClass12InstitutionType(
                            rs.getString("class_12_institution_type"));
                    details.setStudiedGovClass6To12(
                            getNullableBoolean(rs, "studied_gov_class_6_to_12"));
                    details.setStudiedGovClass10To12(
                            getNullableBoolean(rs, "studied_gov_class_10_to_12"));
                    details.setClass10Percentage(
                            rs.getBigDecimal("class_10_percentage"));
                    details.setClass12Percentage(
                            rs.getBigDecimal("class_12_percentage"));

                    int completionYear =
                            rs.getInt("class_12_completion_year");
                    if (!rs.wasNull()) {
                        details.setClass12CompletionYear(completionYear);
                    }

                    return details;
                }, studentId);

        return results.isEmpty() ? null : results.get(0);
    }

    // 5. Retrieve all semester-wise academic records
    public List<StudentRegistrationRequest.AcademicRecordDetails>
            findAcademicRecordsByStudentId(Long studentId) {

        String sql = """
                SELECT semester, cgpa, attendance_percentage,
                       arrears_count, conduct_good, academic_year
                FROM student_academic_records
                WHERE student_id = ?
                ORDER BY semester
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            StudentRegistrationRequest.AcademicRecordDetails record =
                    new StudentRegistrationRequest.AcademicRecordDetails();

            record.setSemester(rs.getInt("semester"));
            record.setCgpa(rs.getBigDecimal("cgpa"));
            record.setAttendancePercentage(
                    rs.getBigDecimal("attendance_percentage"));

            int arrears = rs.getInt("arrears_count");
            if (!rs.wasNull()) {
                record.setArrearsCount(arrears);
            }

            record.setConductGood(
                    getNullableBoolean(rs, "conduct_good"));
            record.setAcademicYear(rs.getString("academic_year"));

            return record;
        }, studentId);
    }

    // Preserve SQL NULL as Java null instead of converting it to false.
    private Boolean getNullableBoolean(ResultSet rs, String column)
            throws SQLException {

        boolean value = rs.getBoolean(column);
        return rs.wasNull() ? null : value;
    }

}
