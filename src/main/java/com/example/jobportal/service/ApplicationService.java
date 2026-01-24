package com.example.jobportal.service;

import com.example.jobportal.entity.Job;
import com.example.jobportal.entity.JobApplication;
import com.example.jobportal.entity.User;
import com.example.jobportal.repository.JobApplicationRepository;
import com.example.jobportal.repository.JobRepository;
import com.example.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {

    @Autowired
    private JobApplicationRepository applicationRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    public JobApplication applyToJob(Long jobId, Long candidateId) {
        if (applicationRepository.existsByJobIdAndCandidateId(jobId, candidateId)) {
            throw new RuntimeException("You have already applied to this job.");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        User candidate = userRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        JobApplication application = new JobApplication();
        application.setJob(job);
        application.setCandidate(candidate);
        return applicationRepository.save(application);
    }

    public void deleteApplication(Long applicationId) {
        applicationRepository.deleteById(applicationId);
    }
}