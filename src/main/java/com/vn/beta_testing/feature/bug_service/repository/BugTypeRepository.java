package com.vn.beta_testing.feature.bug_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vn.beta_testing.domain.BugType;

public interface BugTypeRepository extends JpaRepository<BugType, Long> { }
