
package com.mit.scholarship.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mit.scholarship.algorithm.CompatibilityRule;
import com.mit.scholarship.algorithm.ScholarshipCompatibilityAnalyzer;

@Repository
public class CompatibilityRuleRepository {

    private final JdbcTemplate jdbcTemplate;

    public CompatibilityRuleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<CompatibilityRule> findAll() {
        String sql = """
            SELECT scholarship_id_1, scholarship_id_2, rule_status,
                   restriction_reason, source_reference,
                   last_verified_date
            FROM scholarship_compatibility_rules
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            String status = rs.getString("rule_status");

            ScholarshipCompatibilityAnalyzer.Status ruleStatus =
                status == null ? null :
                ScholarshipCompatibilityAnalyzer.Status.valueOf(
                    status.trim().toUpperCase()
                );

            Date verifiedDate = rs.getDate("last_verified_date");

            return new CompatibilityRule(
                rs.getLong("scholarship_id_1"),
                rs.getLong("scholarship_id_2"),
                ruleStatus,
                rs.getString("restriction_reason"),
                rs.getString("source_reference"),
                verifiedDate == null ? null : verifiedDate.toLocalDate()
            );
        });
    }

    public void save(CompatibilityRule rule) {

        if (rule == null) {
            throw new IllegalArgumentException(
                "Compatibility rule cannot be null"
            );
        }

        if (rule.getScholarshipId1() == rule.getScholarshipId2()) {
            throw new IllegalArgumentException(
                "A scholarship cannot be paired with itself"
            );
        }

        if (rule.getRuleStatus() == null) {
            throw new IllegalArgumentException(
                "Compatibility rule status cannot be null"
            );
        }

        long id1 = Math.min(
            rule.getScholarshipId1(),
            rule.getScholarshipId2()
        );

        long id2 = Math.max(
            rule.getScholarshipId1(),
            rule.getScholarshipId2()
        );

        String sql = """
            INSERT INTO scholarship_compatibility_rules
                (scholarship_id_1, scholarship_id_2, rule_status,
                 restriction_reason, source_reference, last_verified_date)
            VALUES (?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                rule_status = VALUES(rule_status),
                restriction_reason = VALUES(restriction_reason),
                source_reference = VALUES(source_reference),
                last_verified_date = VALUES(last_verified_date)
            """;

        jdbcTemplate.update(
            sql,
            id1,
            id2,
            rule.getRuleStatus().name(),
            rule.getRestrictionReason(),
            rule.getSourceReference(),
            rule.getLastVerifiedDate() == null
                ? null
                : Date.valueOf(rule.getLastVerifiedDate())
        );
    }
}
