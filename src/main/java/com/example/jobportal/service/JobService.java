package com.example.jobportal.service;

import com.example.jobportal.dto.JobRequest;
import com.example.jobportal.entity.Job;
import com.example.jobportal.entity.User;
import com.example.jobportal.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    public Job createJob(JobRequest jobReq, User recruiter) {
        Job job = new Job();
        job.setTitle(jobReq.getTitle());
        job.setDescription(jobReq.getDescription());
        job.setCompany(jobReq.getCompany());
        job.setLocation(jobReq.getLocation());
        job.setRecruiter(recruiter);
        return jobRepository.save(job);
    }

    public Page<Job> getJobs(String location, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (location == null || location.isBlank()) {
            return jobRepository.findAll(pageable);
        } else {
            return jobRepository.findByLocationContainingIgnoreCase(location, pageable);
        }
    }

    public void deleteJob(Long jobId) {
        jobRepository.deleteById(jobId);
    }
}