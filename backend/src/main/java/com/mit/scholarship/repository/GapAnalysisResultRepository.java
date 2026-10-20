
package com.mit.scholarship.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mit.scholarship.dto.GapAnalysisResponse;

@Repository
public class GapAnalysisResultRepository {

    private final JdbcTemplate jdbcTemplate;

    public GapAnalysisResultRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveResults(
            Long analysisId,
            List<GapAnalysisResponse.RequirementGap> results) {

        String sql = """
            INSERT INTO gap_analysis_results
            (analysis_id, attribute, operator, required_value,
             student_value, status, message, gap, mandatory)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        for (GapAnalysisResponse.RequirementGap result : results) {
            jdbcTemplate.update(
                    sql,
                    analysisId,
                    result.getAttribute(),
                    result.getOperator(),
                    result.getRequiredValue(),
                    result.getStudentValue(),
                    result.getStatus(),
                    result.getMessage(),
                    result.getGap(),
                    result.isMandatory()
            );
        }
    }
}
