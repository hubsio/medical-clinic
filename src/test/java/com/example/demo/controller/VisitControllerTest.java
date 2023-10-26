package com.example.demo.controller;

import com.example.demo.model.VisitType;
import com.example.demo.model.dto.BookVisitRequest;
import com.example.demo.model.dto.CreateVisitCommand;
import com.example.demo.model.dto.VisitDTO;
import com.example.demo.model.entity.Visit;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.HealthcareFacilityRepository;
import com.example.demo.repository.VisitRepository;
import com.example.demo.service.VisitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
    private VisitDTO visitDTO;
    @BeforeEach
    public void setUp() {
        visitDTO = VisitDTO.builder()
                .id(1L)
                .doctorId(1L)
                .healthcareFacilityId(2L)
                .startDateTime(LocalDateTime.of(2023, 12, 12, 14, 0))
                .endDateTime(LocalDateTime.of(2023, 12, 12, 14, 15))
                .price(250.00)
                .build();
    }

    @Test
    public void testAddAvailableVisit() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(visitDTO)))
                .andDo(print())
                        .andExpect(status().isOk());
    }

    @Test
    public void testBookVisit() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/visits/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(visitDTO)))
                .andDo(print())
                .andExpect(status().isOk());
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
