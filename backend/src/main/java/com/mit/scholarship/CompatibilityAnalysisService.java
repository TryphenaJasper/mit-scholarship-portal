

package com.mit.scholarship;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mit.scholarship.algorithm.CompatibilityRule;
import com.mit.scholarship.algorithm.ScholarshipCompatibilityAnalyzer;
import com.mit.scholarship.model.Scholarship;
import com.mit.scholarship.repository.CompatibilityRuleRepository;
import com.mit.scholarship.repository.ScholarshipRepository;

@Service
public class CompatibilityAnalysisService {

    private final CompatibilityRuleRepository ruleRepository;
    private final ScholarshipRepository scholarshipRepository;
    private final ScholarshipCompatibilityAnalyzer analyzer;

    public CompatibilityAnalysisService(
            CompatibilityRuleRepository ruleRepository,
            ScholarshipRepository scholarshipRepository) {

        this.ruleRepository = ruleRepository;
        this.scholarshipRepository = scholarshipRepository;
        this.analyzer = new ScholarshipCompatibilityAnalyzer();
    }

    public ScholarshipCompatibilityAnalyzer.Status analyze(
            long scholarshipId1,
            long scholarshipId2) {

        List<Scholarship> scholarships = scholarshipRepository.findAll();

        Scholarship first = scholarships.stream()
                .filter(s -> s.getScholarshipId() == scholarshipId1)
                .findFirst().orElse(null);

        Scholarship second = scholarships.stream()
                .filter(s -> s.getScholarshipId() == scholarshipId2)
                .findFirst().orElse(null);

        if (first == null || second == null
                || scholarshipId1 == scholarshipId2) {
            return ScholarshipCompatibilityAnalyzer.Status.NEEDS_VERIFICATION;
        }

        List<CompatibilityRule> rules = ruleRepository.findAll();
        CompatibilityRule existing =
                analyzer.findRule(scholarshipId1, scholarshipId2, rules);

        if (existing != null
                && existing.getRuleStatus() != null
                && existing.getRuleStatus()
                    != ScholarshipCompatibilityAnalyzer.Status.NEEDS_VERIFICATION) {
            return existing.getRuleStatus();
        }

        return analyzer.classifyByCombinationFlag(
                first.getAllowsCombination(),
                second.getAllowsCombination()
        );
    }

    public List<CompatibilityRule> analyzeAllPairs() {

        List<Scholarship> scholarships = scholarshipRepository.findAll();
        List<CompatibilityRule> existingRules = ruleRepository.findAll();
        List<CompatibilityRule> results = new ArrayList<>();

        for (int i = 0; i < scholarships.size(); i++) {
            for (int j = i + 1; j < scholarships.size(); j++) {

                Scholarship first = scholarships.get(i);
                Scholarship second = scholarships.get(j);

                long id1 = first.getScholarshipId();
                long id2 = second.getScholarshipId();

                CompatibilityRule existing =
                        analyzer.findRule(id1, id2, existingRules);

                if (existing != null
                        && existing.getRuleStatus() != null
                        && existing.getRuleStatus()
                            != ScholarshipCompatibilityAnalyzer.Status.NEEDS_VERIFICATION) {
                    results.add(existing);
                    continue;
                }

                ScholarshipCompatibilityAnalyzer.Status status =
                        analyzer.classifyByCombinationFlag(
                                first.getAllowsCombination(),
                                second.getAllowsCombination()
                        );

                String reason;
                if (status == ScholarshipCompatibilityAnalyzer.Status.COMPATIBLE) {
                    reason = "Both scholarships allow combination under the project's configured rule.";
                } else if (status == ScholarshipCompatibilityAnalyzer.Status.CONFLICTING) {
                    reason = "At least one scholarship does not allow combination under the project's configured rule.";
                } else {
                    reason = "Combination policy requires verification.";
                }

                CompatibilityRule rule = new CompatibilityRule(
                        Math.min(id1, id2),
                        Math.max(id1, id2),
                        status,
                        reason,
                        null,
                        null
                );

                ruleRepository.save(rule);
                results.add(rule);

                if (existing != null) {
                    existingRules.remove(existing);
                }
                existingRules.add(rule);
            }
        }

        return results;
    }
}
