package com.example.demo.controller;

import com.example.demo.model.Patient;
import com.example.demo.repository.PatientRepository;
import com.example.demo.service.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.awt.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class PatientControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PatientRepository patientRepository;
    private Patient samplePatient;

    Patient patient = new Patient("dupa@gmail.com", "123", "1234567", "Tomek", "Nowak", "123-456-789", LocalDate.of(1910, 12, 11));
    @BeforeEach
    public void setUp() {
        Optional<Patient> samplePatient = patientRepository.findByEmail(patient.getEmail());
        if (samplePatient.isEmpty()) {
            patientRepository.save(patient);
        }
    }
    @Test
    void getAllPatientsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/patients")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value(patient.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value(patient.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value(patient.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].phoneNumber").value(patient.getPhoneNumber()));

    }
    @Test
    void getPatientByEmailTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/patients/{email}", patient.getEmail())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(patient.getEmail()));
    }
    @Test
    void addNewPatientTest() throws Exception {
        Patient newPatient = new Patient("newpatient@example.com", "password", "987654321", "John", "Doe", "987-654-321", LocalDate.of(1990, 5, 20));
        mockMvc.perform(MockMvcRequestBuilders.post("/patients")
                .content(objectMapper.writeValueAsString(newPatient))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deletePatientByEmailTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/patients/{email}", patient.getEmail())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void editPatientByEmailTest() throws Exception {
        Patient editedPatient = new Patient("edited@example.com", "newpassword", "1234567", "Edited", "Patient", "987-654-321", LocalDate.of(1985, 6, 15));

        mockMvc.perform(MockMvcRequestBuilders.put("/patients/{email}", patient.getEmail())
                .content(objectMapper.writeValueAsString(editedPatient))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(editedPatient.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(editedPatient.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(editedPatient.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value(editedPatient.getPhoneNumber()));

    }
    @Test
    void updatePasswordTest() throws Exception {
        Patient editedPatient = new Patient("edited@example.com", "newpassword", "1234567", "Edited", "Patient", "987-654-321", LocalDate.of(1985, 6, 15));

        mockMvc.perform(MockMvcRequestBuilders.patch("/patients/{email}/password", patient.getEmail())
                .content(editedPatient.getPassword())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(editedPatient.getPassword()));

    }
}
