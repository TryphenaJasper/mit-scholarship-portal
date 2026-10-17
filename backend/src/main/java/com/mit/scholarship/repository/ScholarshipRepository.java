
package com.mit.scholarship.repository;

import com.mit.scholarship.model.Scholarship;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ScholarshipRepository {

    private final JdbcTemplate jdbcTemplate;

    public ScholarshipRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Scholarship> findAll() {

        String sql = """
            SELECT scholarship_id, name, category, provider,
                   description, amount, currency, amount_period,
                   benefit_type, degree_type, application_type,
                   deadline, official_url, status, last_verified_at
            FROM scholarships
            ORDER BY scholarship_id
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRow(rs));
    }
    public Scholarship findById(Long id) {

    String sql = """
        SELECT scholarship_id, name, category, provider,
               description, amount, currency, amount_period,
               benefit_type, degree_type, application_type,
               deadline, official_url, status, last_verified_at
        FROM scholarships
        WHERE scholarship_id = ?
        """;

    List<Scholarship> results = jdbcTemplate.query(
        sql,
        (rs, rowNum) -> mapRow(rs),
        id
    );

    return results.isEmpty() ? null : results.get(0);
}
    private Scholarship mapRow(ResultSet rs) throws SQLException {

        Scholarship scholarship = new Scholarship();

        scholarship.setScholarshipId(rs.getLong("scholarship_id"));
        scholarship.setName(rs.getString("name"));
        scholarship.setCategory(rs.getString("category"));
        scholarship.setProvider(rs.getString("provider"));
        scholarship.setDescription(rs.getString("description"));
        scholarship.setAmount(rs.getBigDecimal("amount"));
        scholarship.setCurrency(rs.getString("currency"));
        scholarship.setAmountPeriod(rs.getString("amount_period"));
        scholarship.setBenefitType(rs.getString("benefit_type"));
        scholarship.setDegreeType(rs.getString("degree_type"));
        scholarship.setApplicationType(rs.getString("application_type"));

        java.sql.Date deadline = rs.getDate("deadline");
        if (deadline != null) {
            scholarship.setDeadline(deadline.toLocalDate());
        }

        scholarship.setOfficialUrl(rs.getString("official_url"));
        scholarship.setStatus(rs.getString("status"));

        java.sql.Timestamp verifiedAt =
                rs.getTimestamp("last_verified_at");

        if (verifiedAt != null) {
            scholarship.setLastVerifiedAt(
                    verifiedAt.toLocalDateTime()
            );
        }

        return scholarship;
    }
}

