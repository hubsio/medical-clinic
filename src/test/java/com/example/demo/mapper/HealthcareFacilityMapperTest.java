package com.example.demo.mapper;

import com.example.demo.model.dto.HealthcareFacilityDTO;
import com.example.demo.model.entity.Doctor;
import com.example.demo.model.entity.HealthcareFacility;
import com.example.demo.model.mapper.HealthcareFacilityMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HealthcareFacilityMapperTest {
    private final HealthcareFacilityMapper healthcareFacilityMapper = Mappers.getMapper(HealthcareFacilityMapper.class);

    @Test
    void shouldMapFacilityToDto() {
        HealthcareFacility facility = new HealthcareFacility();
        facility.setId(1L);
        facility.setName("Test Facility");
        List<Doctor> doctors = new ArrayList<>();
        Doctor doctor1 = new Doctor();
        doctor1.setId(1L);
        Doctor doctor2 = new Doctor();
        doctor2.setId(2L);
        doctors.add(doctor1);
        doctors.add(doctor2);
        facility.setDoctors(doctors);

        HealthcareFacilityDTO facilityDTO = healthcareFacilityMapper.convertFacilityToDto(facility);

        assertEquals(facility.getId(), facilityDTO.getId());
        assertEquals(facility.getName(), facilityDTO.getName());
        assertEquals(2, facilityDTO.getDoctorIds().size());
        assertEquals(1L, facilityDTO.getDoctorIds().get(0));
        assertEquals(2L, facilityDTO.getDoctorIds().get(1));
    }

    @Test
    void shouldMapDtoToFacility() {
        HealthcareFacilityDTO facilityDTO = new HealthcareFacilityDTO();
        facilityDTO.setId(1L);
        facilityDTO.setName("Test Facility DTO");
        List<Long> doctorIds = new ArrayList<>();
        doctorIds.add(1L);
        doctorIds.add(2L);
        facilityDTO.setDoctorIds(doctorIds);

        HealthcareFacility facility = healthcareFacilityMapper.DTOToFacility(facilityDTO);

        assertEquals(facilityDTO.getId(), facility.getId());
        assertEquals(facilityDTO.getName(), facility.getName());
        assertEquals(2, facility.getDoctors().size());
        assertEquals(1L, facility.getDoctors().get(0).getId());
        assertEquals(2L, facility.getDoctors().get(1).getId());
    }
}
