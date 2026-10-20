
package com.mit.scholarship.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.mit.scholarship.dto.StudentRegistrationRequest;
import com.mit.scholarship.dto.StudentRegistrationRequest.AcademicRecordDetails;
import com.mit.scholarship.dto.StudentRegistrationRequest.BackgroundDetails;
import com.mit.scholarship.dto.StudentRegistrationRequest.EducationDetails;
import com.mit.scholarship.dto.StudentRegistrationRequest.FinancialDetails;
import com.mit.scholarship.model.Student;

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
                   admission_mode, has_other_scholarship,
                   is_beedi_cine_worker_dependent,
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

                details.setAdmissionMode(
                        rs.getString("admission_mode"));

                details.setHasOtherScholarship(
                        getNullableBoolean(rs, "has_other_scholarship"));

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
    
    public void updateStudentFields(Long studentId, JsonNode student) {

        if (student == null || !student.isObject()) {
        throw new IllegalArgumentException(
                "Student details must be a JSON object.");
        }

        String[] allowedFields = {
        "registerNo",
        "name",
        "email",
        "gender",
        "dateOfBirth",
        "department",
        "degree",
        "yearOfStudy",
        "semester"
        };

        StringBuilder sql = new StringBuilder(
            "UPDATE students SET ");
        java.util.List<Object> values = new java.util.ArrayList<>();

        for (String field : allowedFields) {
            if (student.has(field)) {
                if (student.get(field).isNull()) {
                    throw new IllegalArgumentException(
                        field + " cannot be null.");
                }

            String column;

            switch (field) {
                case "registerNo":
                    column = "register_no";
                    break;
                case "dateOfBirth":
                    column = "date_of_birth";
                    break;
                case "yearOfStudy":
                    column = "year_of_study";
                    break;
                default:
                    column = field.replaceAll(
                            "([a-z])([A-Z])", "$1_$2"
                    ).toLowerCase();
            }

            if (!values.isEmpty()) {
                sql.append(", ");
            }

            sql.append(column).append(" = ?");
            values.add(student.get(field).asText());
            }
        }

        if (values.isEmpty()) {
            throw new IllegalArgumentException(
                "No editable student fields were provided.");
        }

        sql.append(" WHERE student_id = ?");
        values.add(studentId);

        int updated = jdbcTemplate.update(sql.toString(), values.toArray());

        if (updated == 0) {
            throw new IllegalArgumentException(
                "Student not found or no values changed.");
        }
    }

    
public void updateBackgroundFields(Long studentId, JsonNode background) {

    if (background == null || !background.isObject()) {
        throw new IllegalArgumentException(
                "Background details must be a JSON object.");
    }

    String[] allowedFields = {
        "community",
        "religion",
        "isFirstGraduate",
        "isDifferentlyAbled",
        "isOrphan",
        "hasSingleParent",
        "admissionMode",
        "hasOtherScholarship",
        "isBeediCineWorkerDependent",
        "isCapfAssamRiflesDependent"
    };

    java.util.Map<String, String> columns =
            new java.util.LinkedHashMap<>();

    columns.put("community", "community");
    columns.put("religion", "religion");
    columns.put("isFirstGraduate", "is_first_graduate");
    columns.put("isDifferentlyAbled", "is_differently_abled");
    columns.put("isOrphan", "is_orphan");
    columns.put("hasSingleParent", "has_single_parent");
    columns.put("admissionMode", "admission_mode");
    columns.put("hasOtherScholarship", "has_other_scholarship");
    columns.put(
            "isBeediCineWorkerDependent",
            "is_beedi_cine_worker_dependent");
    columns.put(
            "isCapfAssamRiflesDependent",
            "is_capf_assam_rifles_dependent");

    StringBuilder sql = new StringBuilder(
            "UPDATE student_background SET ");

    java.util.List<Object> values = new java.util.ArrayList<>();

    for (String field : allowedFields) {

        if (!background.has(field)) {
            continue;
        }

        JsonNode value = background.get(field);

        if (!values.isEmpty()) {
            sql.append(", ");
        }

        sql.append(columns.get(field)).append(" = ?");

        if (value.isNull()) {

            if (!"hasOtherScholarship".equals(field)) {
                throw new IllegalArgumentException(
                        field + " cannot be null.");
            }

            values.add(null);

        } else if (value.isBoolean()) {

            values.add(value.booleanValue());

        } else if (value.isTextual()) {

            values.add(value.textValue());

        } else {
            throw new IllegalArgumentException(
                    "Invalid value type for " + field + ".");
        }
    }

    if (values.isEmpty()) {
        throw new IllegalArgumentException(
                "No editable background fields were provided.");
    }

    sql.append(" WHERE student_id = ?");
    values.add(studentId);

    int updated = jdbcTemplate.update(
            sql.toString(), values.toArray());

    if (updated == 0) {
        throw new IllegalArgumentException(
                "Background record not found.");
    }
}
  
  
public void updateFinancialFields(Long studentId, JsonNode financial) {

    if (financial == null || !financial.isObject()) {
        throw new IllegalArgumentException(
                "Financial details must be a JSON object.");
    }

    String[] allowedFields = {
        "parentalAnnualIncome",
        "parentalMonthlyIncome",
        "incomeCertificateAvailable"
    };

    java.util.Map<String, String> columns = new java.util.LinkedHashMap<>();
    columns.put("parentalAnnualIncome", "parental_annual_income");
    columns.put("parentalMonthlyIncome", "parental_monthly_income");
    columns.put("incomeCertificateAvailable", "income_certificate_available");

    StringBuilder sql = new StringBuilder(
            "UPDATE student_financial SET ");
    java.util.List<Object> values = new java.util.ArrayList<>();

    for (String field : allowedFields) {
        if (financial.has(field)) {
            JsonNode value = financial.get(field);

            if (value.isNull()) {
                throw new IllegalArgumentException(
                        field + " cannot be null.");
            }

            if (!values.isEmpty()) {
                sql.append(", ");
            }

            sql.append(columns.get(field)).append(" = ?");

            if (field.equals("incomeCertificateAvailable")) {
                if (!value.isBoolean()) {
                    throw new IllegalArgumentException(
                            field + " must be true or false.");
                }
                values.add(value.booleanValue());
            } else {
                if (!value.isNumber() || value.decimalValue().signum() < 0) {
                    throw new IllegalArgumentException(
                            field + " must be a non-negative number.");
                }
                values.add(value.decimalValue());
            }
        }
    }

    if (values.isEmpty()) {
        throw new IllegalArgumentException(
                "No editable financial fields were provided.");
    }

    sql.append(" WHERE student_id = ?");
    values.add(studentId);

    int updated = jdbcTemplate.update(sql.toString(), values.toArray());

    if (updated == 0) {
        throw new IllegalArgumentException(
                "Financial record not found.");
    }
}

public void updateEducationFields(Long studentId, JsonNode education) {

    if (education == null || !education.isObject()) {
        throw new IllegalArgumentException(
                "Education details must be a JSON object.");
    }

    String[] allowedFields = {
        "class10InstitutionType",
        "class12InstitutionType",
        "studiedGovClass6To12",
        "studiedGovClass10To12",
        "class10Percentage",
        "class12Percentage",
        "class12CompletionYear"
    };

    java.util.Map<String, String> columns = new java.util.LinkedHashMap<>();
    columns.put("class10InstitutionType", "class_10_institution_type");
    columns.put("class12InstitutionType", "class_12_institution_type");
    columns.put("studiedGovClass6To12", "studied_gov_class_6_to_12");
    columns.put("studiedGovClass10To12", "studied_gov_class_10_to_12");
    columns.put("class10Percentage", "class_10_percentage");
    columns.put("class12Percentage", "class_12_percentage");
    columns.put("class12CompletionYear", "class_12_completion_year");

    StringBuilder sql = new StringBuilder(
            "UPDATE student_education_history SET ");
    java.util.List<Object> values = new java.util.ArrayList<>();

    for (String field : allowedFields) {
        if (education.has(field)) {
            JsonNode value = education.get(field);

            if (value.isNull()) {
                throw new IllegalArgumentException(
                        field + " cannot be null.");
            }

            if (!values.isEmpty()) {
                sql.append(", ");
            }

            sql.append(columns.get(field)).append(" = ?");

            switch (field) {
                case "class10InstitutionType":
                case "class12InstitutionType":
                    if (!value.isTextual()) {
                        throw new IllegalArgumentException(
                                field + " must be text.");
                    }
                    String type = value.textValue().toUpperCase();
                    if (!java.util.Set.of(
                            "GOVERNMENT", "PRIVATE", "AIDED", "UNKNOWN"
                    ).contains(type)) {
                        throw new IllegalArgumentException(
                                "Invalid institution type for " + field + ".");
                    }
                    values.add(type);
                    break;

                case "studiedGovClass6To12":
                case "studiedGovClass10To12":
                    if (!value.isBoolean()) {
                        throw new IllegalArgumentException(
                                field + " must be true or false.");
                    }
                    values.add(value.booleanValue());
                    break;

                case "class10Percentage":
                case "class12Percentage":
                    if (!value.isNumber()
                            || value.decimalValue().signum() < 0
                            || value.decimalValue().compareTo(
                                    java.math.BigDecimal.valueOf(100)) > 0) {
                        throw new IllegalArgumentException(
                                field + " must be between 0 and 100.");
                    }
                    values.add(value.decimalValue());
                    break;

                case "class12CompletionYear":
                    if (!value.canConvertToInt()) {
                        throw new IllegalArgumentException(
                                field + " must be a valid year.");
                    }
                    int year = value.intValue();
                    if (year < 1901 || year > 2155) {
                        throw new IllegalArgumentException(
                                "Invalid class 12 completion year.");
                    }
                    values.add(year);
                    break;
            }
        }
    }

    if (values.isEmpty()) {
        throw new IllegalArgumentException(
                "No editable education fields were provided.");
    }

    // Keep the two government-school flags consistent.
    if (education.has("studiedGovClass6To12")
            && education.get("studiedGovClass6To12").asBoolean()) {
        if (education.has("studiedGovClass10To12")
                && !education.get("studiedGovClass10To12").asBoolean()) {
            throw new IllegalArgumentException(
                    "If studiedGovClass6To12 is true, "
                    + "studiedGovClass10To12 must also be true.");
        }
    }

    sql.append(" WHERE education_id = "
            + "(SELECT education_id FROM "
            + "(SELECT education_id FROM student_education_history "
            + "WHERE student_id = ? ORDER BY education_id DESC LIMIT 1) latest)");

    values.add(studentId);

    int updated = jdbcTemplate.update(sql.toString(), values.toArray());

    if (updated == 0) {
        throw new IllegalArgumentException(
                "Education record not found.");
    }
}

public void updateAcademicFields(
        Long studentId, JsonNode academicRecord) {

    if (academicRecord == null || !academicRecord.isObject()) {
        throw new IllegalArgumentException(
                "Academic record must be a JSON object.");
    }

    if (!academicRecord.has("semester")
            || !academicRecord.get("semester").canConvertToInt()) {
        throw new IllegalArgumentException(
                "Semester is required to identify the academic record.");
    }

    int semester = academicRecord.get("semester").intValue();

    if (semester < 1 || semester > 8) {
        throw new IllegalArgumentException(
                "Semester must be between 1 and 8.");
    }

    String[] allowedFields = {
        "cgpa",
        "attendancePercentage",
        "arrearsCount",
        "conductGood",
        "academicYear"
    };

    java.util.Map<String, String> columns = new java.util.LinkedHashMap<>();
    columns.put("cgpa", "cgpa");
    columns.put("attendancePercentage", "attendance_percentage");
    columns.put("arrearsCount", "arrears_count");
    columns.put("conductGood", "conduct_good");
    columns.put("academicYear", "academic_year");

    StringBuilder sql = new StringBuilder(
            "UPDATE student_academic_records SET ");
    java.util.List<Object> values = new java.util.ArrayList<>();

    for (String field : allowedFields) {
        if (academicRecord.has(field)) {
            JsonNode value = academicRecord.get(field);

            if (value.isNull()) {
                throw new IllegalArgumentException(
                        field + " cannot be null.");
            }

            if (!values.isEmpty()) {
                sql.append(", ");
            }

            sql.append(columns.get(field)).append(" = ?");

            switch (field) {
                case "cgpa":
                    if (!value.isNumber()
                            || value.decimalValue().signum() < 0
                            || value.decimalValue().compareTo(
                                    java.math.BigDecimal.TEN) > 0) {
                        throw new IllegalArgumentException(
                                "CGPA must be between 0 and 10.");
                    }
                    values.add(value.decimalValue());
                    break;

                case "attendancePercentage":
                    if (!value.isNumber()
                            || value.decimalValue().signum() < 0
                            || value.decimalValue().compareTo(
                                    java.math.BigDecimal.valueOf(100)) > 0) {
                        throw new IllegalArgumentException(
                                "Attendance must be between 0 and 100.");
                    }
                    values.add(value.decimalValue());
                    break;

                case "arrearsCount":
                    if (!value.isIntegralNumber()
                            || !value.canConvertToInt()
                            || value.intValue() < 0) {
                        throw new IllegalArgumentException(
                                "Arrears count must be a non-negative integer.");
                    }
                    values.add(value.intValue());
                    break;

                case "conductGood":
                    if (!value.isBoolean()) {
                        throw new IllegalArgumentException(
                                "conductGood must be true or false.");
                    }
                    values.add(value.booleanValue());
                    break;

                case "academicYear":
                    if (!value.isTextual()) {
                        throw new IllegalArgumentException(
                                "academicYear must be text.");
                    }
                    values.add(value.textValue());
                    break;
            }
        }
    }

    if (values.isEmpty()) {
        throw new IllegalArgumentException(
                "No editable academic fields were provided.");
    }

    sql.append(" WHERE student_id = ? AND semester = ?");
    values.add(studentId);
    values.add(semester);

    int updated = jdbcTemplate.update(sql.toString(), values.toArray());

    if (updated == 0) {
        throw new IllegalArgumentException(
                "Academic record not found for semester " + semester + ".");
    }
}


}
