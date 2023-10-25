package com.example.demo.mapper;

import com.example.demo.model.dto.DoctorDTO;
import com.example.demo.model.entity.Doctor;
import com.example.demo.model.mapper.DoctorMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DoctorMapperTest {
    private final DoctorMapper doctorMapper = Mappers.getMapper(DoctorMapper.class);
    @Test
    public void shouldMapDoctorToDoctorDTO() {
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setSpecialization("Cardiologist");

        DoctorDTO doctorDTO = doctorMapper.doctorToDoctorDTO(doctor);

        assertEquals(doctor.getId(), doctorDTO.getId());
        assertEquals(doctor.getFirstName(), doctorDTO.getFirstName());
        assertEquals(doctor.getLastName(), doctorDTO.getLastName());
        assertEquals(doctor.getSpecialization(), doctorDTO.getSpecialization());
    }

    @Test
    public void shouldMapDoctorDTOToDoctor() {
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setId(2L);
        doctorDTO.setFirstName("Jane");
        doctorDTO.setLastName("Smith");
        doctorDTO.setSpecialization("Dermatologist");

        Doctor doctor = doctorMapper.doctorDtoToDoctor(doctorDTO);

        assertEquals(doctorDTO.getId(), doctor.getId());
        assertEquals(doctorDTO.getFirstName(), doctor.getFirstName());
        assertEquals(doctorDTO.getLastName(), doctor.getLastName());
        assertEquals(doctorDTO.getSpecialization(), doctor.getSpecialization());
    }
}
