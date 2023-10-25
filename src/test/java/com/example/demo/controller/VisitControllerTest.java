package com.example.demo.controller;

import com.example.demo.model.dto.CreateVisitCommand;
import com.example.demo.model.dto.VisitDTO;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.HealthcareFacilityRepository;
import com.example.demo.repository.VisitRepository;
import com.example.demo.service.VisitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VisitControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private HealthcareFacilityRepository healthcareFacilityRepository;
    @Autowired
    private VisitRepository visitRepository;
    @MockBean
    private VisitService visitService;
    @Test
    void addAvailableVisit() throws Exception {
        CreateVisitCommand createVisitCommand = new CreateVisitCommand();
        createVisitCommand.setDoctorId(1L);
        createVisitCommand.setHealthcareFacilityId(1L);
        createVisitCommand.setStartDateTime(LocalDateTime.of(2024, 11, 11, 14, 0));
        createVisitCommand.setEndDateTime(LocalDateTime.of(2024, 11, 11, 14, 15));

        mockMvc.perform(MockMvcRequestBuilders.post("/visits")
                        .content(objectMapper.writeValueAsString(createVisitCommand))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.doctorId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.healthcareFacilityId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDateTime").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.endDateTime").exists());
    }

    @Test
    void getPatientVisits() throws Exception {
        Long patientId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/visits/patient/{patientId}", patientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    void getDoctorVisits() throws Exception {
        Long doctorId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/visits/doctor/{doctorId}", doctorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }
}
