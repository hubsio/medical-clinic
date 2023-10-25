package com.example.demo.controller;

import com.example.demo.model.dto.DoctorDTO;
import com.example.demo.model.dto.HealthcareFacilityDTO;
import com.example.demo.model.entity.Doctor;
import com.example.demo.model.entity.HealthcareFacility;
import com.example.demo.model.entity.User;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.HealthcareFacilityRepository;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HealthcareFacilityControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HealthcareFacilityRepository healthcareFacilityRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    private final HealthcareFacilityDTO healthcareFacilityDTO = new HealthcareFacilityDTO(1L, "Facility", null);

    @BeforeEach
    public void setUp() {
        Optional<User> existingUser = userRepository.findByEmail("doctor@test.com");
        if (existingUser.isEmpty()) {
            User user = new User(1L, "Test Doctor", "doctor@test.com", "password", null, null);
            Doctor doctor = new Doctor(1L, "Test", "Doctor", "123-456-789", LocalDate.of(1980, 1, 15), "Specialty", user, null);
            doctorRepository.save(doctor);
        }

        Optional<HealthcareFacility> existingFacility = healthcareFacilityRepository.findById(healthcareFacilityDTO.getId());
        if (existingFacility.isEmpty()) {
            HealthcareFacility facility = new HealthcareFacility(1L, "Facility", null);
            healthcareFacilityRepository.save(facility);
        }
    }

    @Test
    void getAllFacilitiesTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/healthcare-facilities")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(healthcareFacilityDTO.getName()));
    }

    @Test
    void getFacility() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/healthcare-facilities/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(healthcareFacilityDTO.getName()));
    }

    @Test
    @Rollback
    void createFacility() throws Exception {
        HealthcareFacilityDTO newFacility = new HealthcareFacilityDTO(1L, "NewFacility", null);
        mockMvc.perform(MockMvcRequestBuilders.post("/healthcare-facilities")
                        .content(objectMapper.writeValueAsString(newFacility))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Rollback
    void addDoctorToFacility() throws Exception {
        Long facilityId = 1L;
        Long doctorId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.put("/healthcare-facilities/{facilityId}/add-doctor/{doctorId}", facilityId, doctorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
