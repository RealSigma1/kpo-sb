package com.example.filestoringservice.repository;

import com.example.filestoringservice.entity.WorkSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkSubmissionRepository extends JpaRepository<WorkSubmission, Long> {
}