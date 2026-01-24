package com.example.jobportal.controller;

import com.example.jobportal.entity.Job;
import com.example.jobportal.entity.JobApplication;
import com.example.jobportal.repository.JobApplicationRepository;
import com.example.jobportal.security.UserDetailsImpl;
import com.example.jobportal.service.ApplicationService;
import com.example.jobportal.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidate")
@PreAuthorize("hasRole('CANDIDATE')")
public class CandidateController {

    @Autowired
    private JobService jobService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private JobApplicationRepository applicationRepository;

    @GetMapping("/jobs")
    public ResponseEntity<Page<Job>> getJobs(
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Job> jobs = jobService.getJobs(location, page, size);
        return ResponseEntity.ok(jobs);
    }

    @PostMapping("/apply/{jobId}")
    public ResponseEntity<JobApplication> applyToJob(@PathVariable Long jobId, Authentication auth) {
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        JobApplication app = applicationService.applyToJob(jobId, userDetails.getId());
        return ResponseEntity.ok(app);
    }

    @GetMapping("/applications")
    public ResponseEntity<List<JobApplication>> getMyApplications(Authentication auth) {
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        List<JobApplication> apps = applicationRepository.findByCandidateId(userDetails.getId());
        return ResponseEntity.ok(apps);
    }
}