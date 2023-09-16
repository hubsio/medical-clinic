package com.example.demo.repository;

import com.example.demo.model.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientRepository {
    List<Patient> getAllPatients();
    Optional<Patient> findByEmail(String email);
    void save(Patient patient);
    void delete(Patient patient);
}
