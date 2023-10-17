package com.example.demo.repository;

import com.example.demo.model.dto.CreatePatientCommandDTO;
import com.example.demo.model.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

}
