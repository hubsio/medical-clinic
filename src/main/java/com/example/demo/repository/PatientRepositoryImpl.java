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
        return new ArrayList<>(patients);
    }

    @Override
    public Optional<Patient> findByEmail(String email) {
        return patients.stream()
                .filter(patient -> patient.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public void save(Patient patient) {
        patients.add(patient);
    }

    @Override
    public void delete(Patient patient) {
        patients.remove(patient);
    }
}
