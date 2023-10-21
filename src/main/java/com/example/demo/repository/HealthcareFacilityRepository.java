package com.example.demo.repository;

import com.example.demo.model.entity.HealthcareFacility;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthcareFacilityRepository extends JpaRepository<HealthcareFacility, Long> {
}
