package com.example.jobportal.repository;

import com.example.jobportal.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    boolean existsByJobIdAndCandidateId(Long jobId, Long candidateId);
    List<JobApplication> findByJobId(Long jobId);
    List<JobApplication> findByCandidateId(Long candidateId);
}