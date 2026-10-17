
package com.mit.scholarship.service;

import com.mit.scholarship.model.Scholarship;
import com.mit.scholarship.repository.ScholarshipRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScholarshipService {

    private final ScholarshipRepository scholarshipRepository;

    public ScholarshipService(
            ScholarshipRepository scholarshipRepository) {
        this.scholarshipRepository = scholarshipRepository;
    }

    public List<Scholarship> getAllScholarships() {
        return scholarshipRepository.findAll();
    }
}

