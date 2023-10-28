package com.example.demo.controller;

import com.example.demo.model.VisitType;
import com.example.demo.model.dto.BookVisitRequest;
import com.example.demo.model.dto.CreateVisitCommand;
import com.example.demo.model.dto.VisitDTO;
import com.example.demo.model.entity.Doctor;
import com.example.demo.model.entity.HealthcareFacility;
import com.example.demo.model.entity.Patient;
import com.example.demo.model.entity.Visit;
import com.example.demo.model.mapper.VisitMapper;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.HealthcareFacilityRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.VisitRepository;
import com.example.demo.service.VisitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VisitControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private VisitService visitService;
    @Autowired
    private VisitRepository visitRepository;
    @Autowired
    private VisitMapper visitMapper;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private HealthcareFacilityRepository healthcareFacilityRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private ObjectMapper objectMapper;
    private Doctor doctor;
    private HealthcareFacility healthcareFacility;
    private Patient patient;
    private Visit visit;
    private final VisitDTO visitDTO = new VisitDTO(1L, 1L, 150.00, 1L, LocalDateTime.of(2025, 12, 12, 13, 0), LocalDateTime.of(2025, 12, 12, 13, 15), VisitType.CREATED, 1L);

    @BeforeEach
    public void setUp() {
        doctor = new Doctor();
        doctor.setId(1L);

        healthcareFacility = new HealthcareFacility();
        healthcareFacility.setId(1L);

        patient = new Patient();
        patient.setId(1L);

        visit = new Visit();
        visit.setId(1L);
        visit.setDoctor(doctor);
        visit.setStartDateTime(LocalDateTime.of(2025, 12, 12, 13, 0));
        visit.setEndDateTime(LocalDateTime.of(2025, 12, 12, 13, 15));
        visit.setVisitType(VisitType.CREATED);

        doctorRepository.save(doctor);
        healthcareFacilityRepository.save(healthcareFacility);
        patientRepository.save(patient);
        visitRepository.save(visit);
    }

    @Test
    @Rollback
    void testAddAvailableVisit() throws Exception {
        CreateVisitCommand createVisitCommand = new CreateVisitCommand();
        createVisitCommand.setDoctorId(1L);
        createVisitCommand.setHealthcareFacilityId(1L);
        createVisitCommand.setStartDateTime(LocalDateTime.of(2025, 12, 15, 13, 0));
        createVisitCommand.setEndDateTime(LocalDateTime.of(2025, 12, 15, 13, 15));
        createVisitCommand.setPrice(100.0);

        mockMvc.perform(MockMvcRequestBuilders.post("/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createVisitCommand)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.doctorId").value(visitDTO.getDoctorId()));
    }

    @Test
    void testBookVisit() throws Exception {
        BookVisitRequest bookVisitRequest = new BookVisitRequest();
        bookVisitRequest.setVisitId(visit.getId());
        bookVisitRequest.setPatientId(patient.getId());

        mockMvc.perform(MockMvcRequestBuilders.post("/visits/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookVisitRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(visit.getId()));
    }

    @Test
    void getPatientVisits() throws Exception {
        Long patientId = patient.getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/visits/patient/{patientId}", patientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    void getDoctorVisits() throws Exception {
        Long doctorId = doctor.getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/visits/doctor/{doctorId}", doctorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }
}
