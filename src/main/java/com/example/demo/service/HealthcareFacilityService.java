package com.example.demo.service;

import com.example.demo.exception.DoctorNotFoundException;
import com.example.demo.exception.HealthcareFacilityNotFoundException;
import com.example.demo.model.dto.HealthcareFacilityDTO;
import com.example.demo.model.entity.Doctor;
import com.example.demo.model.entity.HealthcareFacility;
import com.example.demo.model.mapper.HealthcareFacilityMapper;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.HealthcareFacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HealthcareFacilityService {
    private final HealthcareFacilityRepository healthcareFacilityRepository;
    private final DoctorRepository doctorRepository;
    private final HealthcareFacilityMapper healthcareFacilityMapper;

    public HealthcareFacilityDTO createHealthcareFacility(HealthcareFacilityDTO healthcareFacilityDTO) {
        HealthcareFacility facility = healthcareFacilityMapper.DTOToFacility(healthcareFacilityDTO);
        HealthcareFacility createdFacility = healthcareFacilityRepository.save(facility);
        return healthcareFacilityMapper.convertFacilityToDto(createdFacility);
    }

    public List<HealthcareFacilityDTO> getAllFacilities() {
        List<HealthcareFacility> facilities = healthcareFacilityRepository.findAll();
        return facilities.stream()
                .map(healthcareFacilityMapper::convertFacilityToDto)
                .collect(Collectors.toList());
    }

    public HealthcareFacilityDTO getFacility(Long id) {
        HealthcareFacility facility = healthcareFacilityRepository.findById(id)
                .orElseThrow(() -> new HealthcareFacilityNotFoundException("Healthcare Facility not found"));
        return healthcareFacilityMapper.convertFacilityToDto(facility);
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
