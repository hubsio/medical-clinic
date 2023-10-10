package com.example.demo.controller;

import com.example.demo.model.Patient;
import com.example.demo.model.dto.PatientDTO;
import com.example.demo.repository.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Optional;

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

    private PatientDTO patientDTO = new PatientDTO("dupa@gmail.com", "Tomek", "Nowak", "123-456-789");

    @BeforeEach
    public void setUp() {
        Optional<Patient> existingPatient = patientRepository.findByEmail(patientDTO.getEmail());
        if (existingPatient.isEmpty()) {
            Patient patient = new Patient(1L,"dupa@gmail.com", "123", "1234567", "Tomek", "Nowak", "123-456-789", LocalDate.of(1910, 12, 11));
            patientRepository.save(patient);
        }
    }

    @Test
    void getAllPatientsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/patients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value(patientDTO.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value(patientDTO.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value(patientDTO.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].phoneNumber").value(patientDTO.getPhoneNumber()));
    }

    @Test
    void getPatientByEmailTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/patients/{email}", patientDTO.getEmail())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(patientDTO.getEmail()));
    }

    @Test
    void addNewPatientTest() throws Exception {
        PatientDTO newPatient = new PatientDTO("newpatient@example.com", "John", "Doe", "987-654-321");
        mockMvc.perform(MockMvcRequestBuilders.post("/patients")
                        .content(objectMapper.writeValueAsString(newPatient))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deletePatientByEmailTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/patients/{email}", patientDTO.getEmail())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void editPatientByEmailTest() throws Exception {
        Patient editedPatient = new Patient(1L,"dupa@gmail.com", "123123", "1234567", "Tomek123", "Nowak123", "123-456-789", LocalDate.of(1910, 12, 11));

        mockMvc.perform(MockMvcRequestBuilders.put("/patients/{email}", patientDTO.getEmail())
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
        String newPassword = "newpassword";

        mockMvc.perform(MockMvcRequestBuilders.patch("/patients/{email}/password", patientDTO.getEmail())
                        .content(newPassword)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(newPassword));
    }
}

