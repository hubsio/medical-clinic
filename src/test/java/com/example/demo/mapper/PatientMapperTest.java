package com.example.demo.mapper;

import com.example.demo.model.dto.PatientDTO;
import com.example.demo.model.entity.Patient;
import com.example.demo.model.entity.User;
import com.example.demo.model.mapper.PatientMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatientMapperTest {
    private final PatientMapper patientMapper = Mappers.getMapper(PatientMapper.class);

    @Test
    public void shouldMapPatientToPatientDTO() {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("Alice");
        patient.setLastName("Johnson");

        User user = new User();
        user.setEmail("alice@example.com");
        patient.setUser(user);

        PatientDTO patientDTO = patientMapper.patientToPatientDTO(patient);

        assertEquals(patient.getId(), patientDTO.getId());
        assertEquals(patient.getFirstName(), patientDTO.getFirstName());
        assertEquals(patient.getLastName(), patientDTO.getLastName());
        assertEquals(user.getEmail(), patientDTO.getEmail());
    }

    @Test
    void patientDtoToPatient() {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setId(1L);
        patientDTO.setFirstName("John");
        patientDTO.setLastName("Doe");

        Patient patient = patientMapper.patientDtoToPatient(patientDTO);

        assertEquals(1L, patient.getId());
        assertEquals("John", patient.getFirstName());
        assertEquals("Doe", patient.getLastName());
    }
}
