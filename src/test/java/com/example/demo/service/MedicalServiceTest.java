package com.example.demo.service;

import com.example.demo.exception.IllegalUserIdChangeException;
import com.example.demo.exception.InvalidEmailException;
import com.example.demo.exception.PatientNotFoundException;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.model.dto.CreatePatientCommandDTO;
import com.example.demo.model.dto.EditPatientCommandDTO;
import com.example.demo.model.entity.Patient;
import com.example.demo.model.dto.PatientDTO;
import com.example.demo.model.entity.User;
import com.example.demo.model.mapper.PatientMapper;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MedicalServiceTest {
    PatientRepository patientRepository;
    UserRepository userRepository;
    PatientService patientService;
    PatientMapper patientMapper;
    @BeforeEach
    void setUp() {
        this.patientRepository = Mockito.mock(PatientRepository.class);
        this.userRepository = Mockito.mock(UserRepository.class);
        this.patientMapper = PatientMapper.INSTANCE;
        this.patientService = new PatientService(patientRepository, patientMapper, userRepository);
    }

    @Test
    void getPatient_existingPatient_shouldReturnPatient() {
        Patient patient = new Patient(1L, "123", "Tomek", "Nowak", "123456789", LocalDate.of(1990, 12, 12), new User());

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        PatientDTO expectedPatientDTO = PatientMapper.INSTANCE.patientToPatientDTO(patient);
        PatientDTO actualPatient = patientService.getPatient(1L);

        assertEquals(expectedPatientDTO, actualPatient);
    }

    @Test
    void getAllPatients_existingPatients_shouldReturnPatients() {
        List<Patient> expectedPatients = new ArrayList<>();
        expectedPatients.add(new Patient(1L, "123", "Tomek", "Nowak", "123-456-789", LocalDate.of(1990, 12, 12), new User()));
        expectedPatients.add(new Patient(2L, "12345", "Krzychu", "Janusz", "987-654-321", LocalDate.of(1990, 12, 12), new User()));

        when(patientRepository.findAll()).thenReturn(expectedPatients);

        List<PatientDTO> result = patientService.getAllPatients();

        assertEquals(2, result.size());
        assertEquals("Tomek", result.get(0).getFirstName());
        assertEquals("Nowak", result.get(0).getLastName());
        assertEquals("123-456-789", result.get(0).getPhoneNumber());

        assertEquals("Krzychu", result.get(1).getFirstName());
        assertEquals("Janusz", result.get(1).getLastName());
        assertEquals("987-654-321", result.get(1).getPhoneNumber());
    }

    @Test
    void deletePatient_existingPatients_getPatients() {
        Patient patient = new Patient(1L, "123", "Tomek", "Nowak", "123456789", LocalDate.of(1990, 12, 12), new User());

        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));

        patientService.deletePatient(patient.getId());

        verify(patientRepository, times(1)).delete(patient);
    }

    @Test
    void addNewPatient_validData_shouldReturnNewPatientDTO() {
        String email = "john@example.com";
        CreatePatientCommandDTO commandDTO = new CreatePatientCommandDTO("123456789", "John", "Doe", "123-456-789", LocalDate.of(1990, 5, 15), "john_doe", email, "password123");

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        PatientDTO resultDTO = patientService.addNewPatient(commandDTO);

        assertEquals("john@example.com", resultDTO.getEmail());
        assertEquals("John", resultDTO.getFirstName());
        assertEquals("Doe", resultDTO.getLastName());
        assertEquals("123-456-789", resultDTO.getPhoneNumber());
        verify(patientRepository, times(1)).save(any());
    }

    @Test
    void editPatientById_existingPatient_shouldEditPatient() {
        long patientId = 1L;
        Patient existingPatient = new Patient(patientId, "123", "Tomek", "Nowak", "123456789", LocalDate.of(1990, 12, 12), new User());
        EditPatientCommandDTO editedPatient = new EditPatientCommandDTO("NewFirstName", "NewLastName", "NewPhoneNumber", "NewPassword");

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(existingPatient));

        PatientDTO updatedPatientDTO = patientService.editPatientById(patientId, editedPatient);

        assertEquals("NewFirstName", updatedPatientDTO.getFirstName());
        assertEquals("NewLastName", updatedPatientDTO.getLastName());
        assertEquals("NewPhoneNumber", updatedPatientDTO.getPhoneNumber());
        verify(patientRepository, times(1)).save(existingPatient);
    }

    @Test
    void updatePassword_existingPatient_shouldUpdatePassword() {
        Long patientId = 1L;
        String newPassword = "newpassword";
        Patient existingPatient = new Patient();
        existingPatient.setId(patientId);
        existingPatient.setUser(new User());

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(existingPatient));

        String result = patientService.updatePassword(patientId, newPassword);

        assertEquals(newPassword, result);
    }

    @Test
    void getPatient_nonexistentPatient_shouldThrowException() {
        Long nonExistentPatientId = 100L;

        when(patientRepository.findById(nonExistentPatientId)).thenReturn(Optional.empty());

        PatientNotFoundException exception = assertThrows(PatientNotFoundException.class, () -> patientService.getPatient(nonExistentPatientId));

        assertEquals("Patient with the provided ID does not exist", exception.getMessage());
    }

    @Test
    void addNewPatient_emptyEmail_shouldThrowException() {
        CreatePatientCommandDTO patient = new CreatePatientCommandDTO("123456789", "Tomek", "Nowak", "123-456-789", LocalDate.of(1990, 12, 12), "tomek123", null, "password123");

        InvalidEmailException exception = assertThrows(InvalidEmailException.class, () -> patientService.addNewPatient(patient));

        assertEquals("Invalid email address", exception.getMessage());
    }

    @Test
    void addNewPatient_existingEmail_shouldThrowException() {
        CreatePatientCommandDTO createPatientCommandDTO = new CreatePatientCommandDTO("123", "Hubert", "Nowak", "123-456-789", LocalDate.of(1990,12,12), "hubi123", "hubi123@gmail.com", "123321");
        User user = new User(1L, "Jarek", "jarek123@gmail.com", "1321", null);

        when(userRepository.findByEmail("hubi123@gmail.com")).thenReturn(Optional.of(user));

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> patientService.addNewPatient(createPatientCommandDTO));

        assertEquals("User with the provided email already exists", exception.getMessage());
    }

    @Test
    void deletePatient_patientNotFound_shouldThrowException() {
        Long nonExistentPatientId = 100L;

        when(patientRepository.findById(nonExistentPatientId)).thenReturn(Optional.empty());

        PatientNotFoundException exception = assertThrows(PatientNotFoundException.class, () -> patientService.deletePatient(nonExistentPatientId));

        assertEquals("Patient with the provided ID does not exist", exception.getMessage());
    }

    @Test
    void editPatientById_patientNotFound_shouldThrowException() {
        EditPatientCommandDTO editedPatient = new EditPatientCommandDTO("FirstName", "LastName", "123456789", "password");

        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        PatientNotFoundException exception = assertThrows(PatientNotFoundException.class, () -> patientService.editPatientById(1L, editedPatient));

        assertEquals("Patient with given ID does not exist", exception.getMessage());
    }

    @Test
    void editPatientById_nullFields_shouldThrowException() {
        Patient existingPatient = new Patient();
        existingPatient.setId(1L);
        EditPatientCommandDTO editedPatient = new EditPatientCommandDTO(null, null, null, null);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(existingPatient));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> patientService.editPatientById(1L, editedPatient));

        assertEquals("Patient data cannot be null", exception.getMessage());
    }

    @Test
    void updatePassword_patientNotFound_shouldThrowException() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        PatientNotFoundException exception = assertThrows(PatientNotFoundException.class, () -> patientService.updatePassword(1L, "newpassword"));

        assertEquals("Patient with given ID does not exist", exception.getMessage());
    }
    @Test
    void updatePassword_nullPassword_shouldThrowException() {
        Patient existingPatient = new Patient();
        existingPatient.setId(1L);
        when(patientRepository.findById(1L)).thenReturn(Optional.of(existingPatient));

        InvalidEmailException exception = assertThrows(InvalidEmailException.class, () -> patientService.updatePassword(1L, null));

        assertEquals("New password cannot be empty", exception.getMessage());
    }

    @Test
    void updatePassword_emptyPassword_shouldThrowException() {
        Patient existingPatient = new Patient();
        when(patientRepository.findById(1L)).thenReturn(Optional.of(existingPatient));

        InvalidEmailException exception = assertThrows(InvalidEmailException.class, () -> patientService.updatePassword(1L, ""));

        assertEquals("New password cannot be empty", exception.getMessage());
    }
}