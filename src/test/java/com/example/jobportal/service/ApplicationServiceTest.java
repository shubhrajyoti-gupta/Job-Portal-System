package com.example.jobportal.service;

import com.example.jobportal.entity.Job;
import com.example.jobportal.entity.JobApplication;
import com.example.jobportal.entity.User;
import com.example.jobportal.repository.JobApplicationRepository;
import com.example.jobportal.repository.JobRepository;
import com.example.jobportal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceTest {

    @Mock
    private JobApplicationRepository applicationRepository;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ApplicationService applicationService;

    private Job job;
    private User candidate;

    @BeforeEach
    void setUp() {
        job = new Job();
        job.setId(1L);
        candidate = new User();
        candidate.setId(2L);
    }

    @Test
    void applyToJob_shouldThrowIfAlreadyApplied() {
        when(applicationRepository.existsByJobIdAndCandidateId(1L, 2L)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            applicationService.applyToJob(1L, 2L);
        });

        assertEquals("You have already applied to this job.", exception.getMessage());
        verify(applicationRepository, never()).save(any());
    }

    @Test
    void applyToJob_shouldSaveNewApplication() {
        when(applicationRepository.existsByJobIdAndCandidateId(1L, 2L)).thenReturn(false);
        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));
        when(userRepository.findById(2L)).thenReturn(Optional.of(candidate));
        when(applicationRepository.save(any(JobApplication.class))).thenAnswer(invocation -> invocation.getArgument(0));

        JobApplication result = applicationService.applyToJob(1L, 2L);

        assertNotNull(result);
        assertEquals(job, result.getJob());
        assertEquals(candidate, result.getCandidate());
        verify(applicationRepository).save(any(JobApplication.class));
    }
}