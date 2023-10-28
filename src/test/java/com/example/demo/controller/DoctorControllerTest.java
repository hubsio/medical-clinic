package com.example.demo.controller;

import com.example.demo.model.dto.CreateDoctorCommandDTO;
import com.example.demo.model.dto.DoctorDTO;
import com.example.demo.model.dto.EditDoctorCommandDTO;
import com.example.demo.model.dto.PatientDTO;
import com.example.demo.model.entity.Doctor;
import com.example.demo.model.entity.HealthcareFacility;
import com.example.demo.model.entity.Patient;
import com.example.demo.model.entity.User;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
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
public class DoctorControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private UserRepository userRepository;
    private final DoctorDTO doctorDTO = new DoctorDTO(1L,"Tomek", "Nowak", "dupa@gmail.com", "123-456-789", "Kolanoskopia");
    @BeforeEach
    public void setUp() {
        Optional<User> existingPatient = userRepository.findByEmail(doctorDTO.getEmail());
        if (existingPatient.isEmpty()) {
            User user = new User(1L, "Tomek", "dupa@gmail.com", "123", null, null);
            Doctor doctor = new Doctor(1L, "Tomek", "Nowak", "123-456-789", LocalDate.of(1120,12,10), "Kolanoskopia", user, null);
            doctorRepository.save(doctor);
        }
    }

    @Test
    void getAllDoctorsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/doctors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value(doctorDTO.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value(doctorDTO.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value(doctorDTO.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].phoneNumber").value(doctorDTO.getPhoneNumber()));
    }

    @Test
    void getDoctorByIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/doctors/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(doctorDTO.getId()));
    }

    @Test
    @Rollback
    void addNewDoctorTest() throws Exception {
        CreateDoctorCommandDTO newDoctor = new CreateDoctorCommandDTO("123", "John", "Doe", "987-654-321", LocalDate.of(1990, 5, 15), "john_doe", "mail@gmail.com", "12345", "Okulista");
        mockMvc.perform(MockMvcRequestBuilders.post("/doctors")
                        .content(objectMapper.writeValueAsString(newDoctor))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Rollback
    void deleteDoctorByIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/doctors/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Rollback
    void editDoctorByIdTest() throws Exception {
        EditDoctorCommandDTO editedDoctor = new EditDoctorCommandDTO("EditedFirstName", "EditedLastName", "newpassword", "123-456-789", "Ginekolog");
        mockMvc.perform(MockMvcRequestBuilders.put("/doctors/{id}", 1L)
                        .content(objectMapper.writeValueAsString(editedDoctor))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(editedDoctor.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(editedDoctor.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value(editedDoctor.getPhoneNumber()));
    }

    @Test
    @Rollback
    void updateDoctorPasswordTest() throws Exception {
        String newPassword = "newpassword";
        mockMvc.perform(MockMvcRequestBuilders.patch("/doctors/{id}/password", 1L)
                        .content(newPassword)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(newPassword));
    }
}
