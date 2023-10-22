package com.example.demo.repository;

import com.example.demo.model.entity.Visit;
import com.example.demo.service.VisitService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepository extends JpaRepository<Visit, Long> {
}
