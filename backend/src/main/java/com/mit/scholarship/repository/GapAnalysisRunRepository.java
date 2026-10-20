
package com.mit.scholarship.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class GapAnalysisRunRepository {

    private final JdbcTemplate jdbcTemplate;

    public GapAnalysisRunRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long saveRun(
            Long studentId,
            Long scholarshipId,
            boolean eligible,
            int unmetCount,
            int unknownCount) {

        String sql = """
            INSERT INTO gap_analysis_runs
            (student_id, scholarship_id, eligible,
             unmet_count, unknown_count, analyzed_at)
            VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS);

            ps.setLong(1, studentId);
            ps.setLong(2, scholarshipId);
            ps.setBoolean(3, eligible);
            ps.setInt(4, unmetCount);
            ps.setInt(5, unknownCount);

            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();

        if (key == null) {
            throw new IllegalStateException(
                    "Could not retrieve the generated analysis ID.");
        }

        return key.longValue();
    }
}
