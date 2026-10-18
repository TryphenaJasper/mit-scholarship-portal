package com.mit.scholarship.repository;

import com.mit.scholarship.model.ScholarshipRequirement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ScholarshipRequirementRepository {

    private final JdbcTemplate jdbcTemplate;

    public ScholarshipRequirementRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ScholarshipRequirement> findByScholarshipId(Long scholarshipId) {

        String sql = """
                SELECT
                    requirement_id,
                    scholarship_id,
                    requirement_type,
                    attribute,
                    operator,
                    required_value,
                    is_mandatory,
                    applicability_stage,
                    failure_message
                FROM scholarship_requirements
                WHERE scholarship_id = ?
                ORDER BY requirement_id
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {

            ScholarshipRequirement requirement =
                    new ScholarshipRequirement();

            requirement.setRequirementId(
                    rs.getLong("requirement_id"));

            requirement.setScholarshipId(
                    rs.getLong("scholarship_id"));

            requirement.setRequirementType(
                    rs.getString("requirement_type"));

            requirement.setAttribute(
                    rs.getString("attribute"));

            requirement.setOperator(
                    rs.getString("operator"));

            requirement.setRequiredValue(
                    rs.getString("required_value"));

            requirement.setMandatory(
                    rs.getBoolean("is_mandatory"));

            requirement.setApplicabilityStage(
                    rs.getString("applicability_stage"));

            requirement.setFailureMessage(
                    rs.getString("failure_message"));

            return requirement;
        }, scholarshipId);
    }
}