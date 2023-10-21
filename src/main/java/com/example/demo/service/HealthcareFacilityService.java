package com.example.demo.service;

import com.example.demo.exception.DoctorNotFoundException;
import com.example.demo.exception.HealthcareFacilityNotFoundException;
import com.example.demo.model.entity.Doctor;
import com.example.demo.model.entity.HealthcareFacility;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.HealthcareFacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HealthcareFacilityService {
    private final HealthcareFacilityRepository healthcareFacilityRepository;
    private final DoctorRepository doctorRepository;

    public HealthcareFacility createHealthcareFacility(HealthcareFacility facility) {
        return healthcareFacilityRepository.save(facility);
    }

    public List<HealthcareFacility> getAllFacilities() {
        return healthcareFacilityRepository.findAll();
    }

    public HealthcareFacility getFacility(Long id) {
        return healthcareFacilityRepository.findById(id)
                .orElseThrow(() -> new HealthcareFacilityNotFoundException("Healthcare Facility not found"));
    }

    public void addDoctorToFacility(Long facilityId, Long doctorId) {
        HealthcareFacility facility = healthcareFacilityRepository.findById(facilityId)
                .orElseThrow(() -> new HealthcareFacilityNotFoundException("Healthcare Facility not found"));
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new HealthcareFacilityNotFoundException("Doctor not found"));
        facility.getDoctors().add(doctor);
        healthcareFacilityRepository.save(facility);
    }
}
