package com.example.demo.service;

import com.example.demo.exception.DoctorNotFoundException;
import com.example.demo.exception.HealthcareFacilityNotFoundException;
import com.example.demo.model.dto.HealthcareFacilityDTO;
import com.example.demo.model.entity.Doctor;
import com.example.demo.model.entity.HealthcareFacility;
import com.example.demo.model.mapper.HealthcareFacilityMapper;
import com.example.demo.model.mapper.PatientMapper;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.HealthcareFacilityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class HealthcareFacilityTest {
    HealthcareFacilityRepository healthcareFacilityRepository;
    DoctorRepository doctorRepository;
    HealthcareFacilityService healthcareFacilityService;
    HealthcareFacilityMapper healthcareFacilityMapper;

    @BeforeEach
    void setUp() {
        this.healthcareFacilityRepository = Mockito.mock(HealthcareFacilityRepository.class);
        this.doctorRepository = Mockito.mock(DoctorRepository.class);
        this.healthcareFacilityMapper = HealthcareFacilityMapper.INSTANCE;
        this.healthcareFacilityService = new HealthcareFacilityService(healthcareFacilityRepository, doctorRepository, healthcareFacilityMapper);
    }

    @Test
    void createHealthcareFacility_healthcareFacility_shouldCreate() {
        HealthcareFacilityDTO healthcareFacilityDTO = new HealthcareFacilityDTO();
        HealthcareFacility facility = healthcareFacilityMapper.DTOToFacility(healthcareFacilityDTO);

        when(healthcareFacilityRepository.save(any(HealthcareFacility.class))).thenReturn(facility);

        HealthcareFacilityDTO result = healthcareFacilityService.createHealthcareFacility(healthcareFacilityDTO);

        assertEquals(facility.getId(), result.getId());
    }

    @Test
    void getAllFacilities_listOfFacilities_shouldReturn() {
        List<HealthcareFacility> expectedFacilities = new ArrayList<>();
        expectedFacilities.add(new HealthcareFacility());
        expectedFacilities.add(new HealthcareFacility());

        when(healthcareFacilityRepository.findAll()).thenReturn(expectedFacilities);

        List<HealthcareFacilityDTO> facilityDTOList = healthcareFacilityService.getAllFacilities();

        assertEquals(expectedFacilities.size(), facilityDTOList.size());
        assertEquals(expectedFacilities.get(0).getId(), facilityDTOList.get(0).getId());
        assertEquals(expectedFacilities.get(1).getId(), facilityDTOList.get(1).getId());
    }

    @Test
    void getFacility_facilityById_shouldBePositive() {
        Long facilityId = 1L;
        HealthcareFacility facility = new HealthcareFacility();
        facility.setId(facilityId);

        when(healthcareFacilityRepository.findById(facilityId)).thenReturn(Optional.of(facility));

        HealthcareFacilityDTO facilityDTO = healthcareFacilityService.getFacility(facilityId);

        assertEquals(facilityId, facilityDTO.getId());
    }

    @Test
    void addDoctorToFacility_byId_shouldAddDoctor() {
        Long facilityId = 1L;
        Long doctorId = 2L;
        HealthcareFacility facility = new HealthcareFacility();
        facility.setId(facilityId);
        Doctor doctor = new Doctor();
        doctor.setId(doctorId);

        when(healthcareFacilityRepository.findById(facilityId)).thenReturn(Optional.of(facility));
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));

        healthcareFacilityService.addDoctorToFacility(facilityId, doctorId);

        verify(healthcareFacilityRepository, times(1)).findById(facilityId);
        verify(doctorRepository, times(1)).findById(doctorId);
        verify(healthcareFacilityRepository, times(1)).save(facility);
    }

    @Test
    void getFacility_facilityById_shouldBeNegative() {
        Long facilityId = 1L;
        when(healthcareFacilityRepository.findById(facilityId)).thenReturn(Optional.empty());

        HealthcareFacilityNotFoundException exception = assertThrows(HealthcareFacilityNotFoundException.class, () -> healthcareFacilityService.getFacility(facilityId));

        assertEquals("Healthcare Facility not found", exception.getMessage());
    }

    @Test
    void addDoctorToFacility_facilityNotFound_shouldThrowException() {
        Long facilityId = 1L;
        Long doctorId = 2L;

        when(healthcareFacilityRepository.findById(facilityId)).thenReturn(Optional.empty());

        HealthcareFacilityNotFoundException exception = assertThrows(HealthcareFacilityNotFoundException.class, () -> healthcareFacilityService.addDoctorToFacility(facilityId, doctorId));

        assertEquals("Healthcare Facility not found", exception.getMessage());
    }

    @Test
    public void addDoctorToFacility_doctorNotFound_shouldThrowDoctorException() {
        Long facilityId = 1L;
        Long doctorId = 2L;

        when(healthcareFacilityRepository.findById(facilityId)).thenReturn(Optional.of(new HealthcareFacility()));
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());

        DoctorNotFoundException exception = assertThrows(DoctorNotFoundException.class, () -> healthcareFacilityService.addDoctorToFacility(facilityId, doctorId));

        assertEquals("Doctor not found", exception.getMessage());
    }
}
