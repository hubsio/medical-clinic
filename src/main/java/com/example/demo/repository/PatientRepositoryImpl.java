package com.example.demo.repository;

import com.example.demo.model.Patient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWarDeployment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PatientRepositoryImpl implements PatientRepository {
    private final List<Patient> patients = new ArrayList<>();

    @Override
    public List<Patient> getAllPatients() {
        return null;
    }

    @Override
    public Optional<Patient> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public void save(Patient patient) {

    }

    @Override
    public void delete(Patient patient) {

    }
}
