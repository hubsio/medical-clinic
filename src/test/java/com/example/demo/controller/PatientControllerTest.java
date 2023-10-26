package com.example.demo.controller;

import com.example.demo.model.dto.CreatePatientCommandDTO;
import com.example.demo.model.dto.EditPatientCommandDTO;
import com.example.demo.model.entity.Patient;
import com.example.demo.model.dto.PatientDTO;
import com.example.demo.model.entity.User;
import com.example.demo.repository.PatientRepository;
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
    @Autowired
    private UserRepository userRepository;
    private final PatientDTO patientDTO = new PatientDTO(1L,"dupa@gmail.com", "Tomek", "Nowak", "123-456-789");

    @BeforeEach
    public void setUp() {
        Optional<User> existingPatient = userRepository.findByEmail(patientDTO.getEmail());
        if (existingPatient.isEmpty()) {
            User user = new User(1L, "Tomek123", "dupa@gmail.com", "123", null, null);
            Patient patient = new Patient(1L, "123", "Tomek", "Nowak", "123-456-789", LocalDate.of(1990, 12, 12), user);
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
    void getPatientByIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/patients/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(patientDTO.getId()));
    }

    @Test
    @Rollback
    void addNewPatientTest() throws Exception {
        CreatePatientCommandDTO newPatient = new CreatePatientCommandDTO("123", "John", "Doe", "987-654-321", LocalDate.of(1990, 5, 15), "john_doe", "mail@gmail.com", "12345");
        mockMvc.perform(MockMvcRequestBuilders.post("/patients")
                        .content(objectMapper.writeValueAsString(newPatient))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Rollback
    void deletePatientByIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/patients/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Rollback
    void editPatientByIdTest() throws Exception {
        EditPatientCommandDTO editedPatient = new EditPatientCommandDTO("EditedFirstName", "EditedLastName", "987-654-321", "newpassword");

        mockMvc.perform(MockMvcRequestBuilders.put("/patients/{id}", 1L)
                        .content(objectMapper.writeValueAsString(editedPatient))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(editedPatient.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(editedPatient.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value(editedPatient.getPhoneNumber()));
    }

    @Test
    @Rollback
    void updatePasswordTest() throws Exception {
        String newPassword = "newpassword";
        mockMvc.perform(MockMvcRequestBuilders.patch("/patients/{id}/password", 1L)
                        .content(newPassword)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(newPassword));
    }
}

