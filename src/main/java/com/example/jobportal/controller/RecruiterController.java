package com.example.jobportal.controller;

import com.example.jobportal.dto.JobRequest;
import com.example.jobportal.entity.Job;
import com.example.jobportal.entity.JobApplication;
import com.example.jobportal.entity.User;
import com.example.jobportal.repository.JobApplicationRepository;
import com.example.jobportal.security.UserDetailsImpl;
import com.example.jobportal.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recruiter")
@PreAuthorize("hasRole('RECRUITER')")
public class RecruiterController {

    @Autowired
    private JobService jobService;

    @Autowired
    private JobApplicationRepository applicationRepository;

    // 👇 ADD THIS — we need UserRepository to fetch User by ID
    @Autowired
    private com.example.jobportal.repository.UserRepository userRepository;

    @PostMapping("/jobs")
    public ResponseEntity<Job> createJob(@RequestBody JobRequest jobReq, Authentication auth) {
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        // ✅ Fetch the actual User entity from DB
        User recruiter = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        Job job = jobService.createJob(jobReq, recruiter);
        return ResponseEntity.ok(job);
    }

    @GetMapping("/jobs/{jobId}/applicants")
    public ResponseEntity<List<JobApplication>> getApplicants(@PathVariable Long jobId) {
        List<JobApplication> apps = applicationRepository.findByJobId(jobId);
        return ResponseEntity.ok(apps);
    }
}