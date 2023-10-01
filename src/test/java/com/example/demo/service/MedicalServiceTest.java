package com.example.demo.service;

import com.example.demo.model.Patient;
import com.example.demo.repository.PatientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicalServiceTest {
    @Mock
    PatientRepository patientRepository;
    @InjectMocks
    PatientService patientService;

    @Test
    void getPatientByEmail_existingPatient_shouldReturnPatient() {
        Patient patient = new Patient();
        patient.setEmail("dupa@gmail.com");
        patient.setFirstName("Hubert");
        patient.setLastName("Nowak");

        when(patientRepository.findByEmail("dupa@gmail.com")).thenReturn(Optional.of(patient));

        Patient actualPatient = patientService.getPatientByEmail("dupa@gmail.com");

        assertEquals("dupa@gmail.com", actualPatient.getEmail());
        assertEquals("Hubert", actualPatient.getFirstName());
        assertEquals("Nowak", actualPatient.getLastName());
    }

    @Test
    void getAllPatients_existingPatients_shouldReturnPatients() {
        List<Patient> expectedPatients = new ArrayList<>();
        expectedPatients.add(new Patient("dupa@gmail.com", "123", "1234567", "Tomek", "Nowak", "123-456-789", LocalDate.of(1910, 12, 11)));
        expectedPatients.add(new Patient("lol@gmail.com", "123432", "13464367", "Krzychu", "Janusz", "987-654-321", LocalDate.of(1910, 12, 11)));

        when(patientRepository.getAllPatients()).thenReturn(expectedPatients);

        List<Patient> result = patientService.getAllPatients();

        assertEquals(2, result.size());
        assertEquals("dupa@gmail.com", result.get(0).getEmail());
        assertEquals("123", result.get(0).getPassword());
        assertEquals("1234567", result.get(0).getIdCardNo());
        assertEquals("Tomek", result.get(0).getFirstName());
        assertEquals("Nowak", result.get(0).getLastName());
        assertEquals("123-456-789", result.get(0).getPhoneNumber());
        assertEquals(LocalDate.of(1910, 12, 11), result.get(0).getBirthday());

        assertEquals("lol@gmail.com", result.get(1).getEmail());
        assertEquals("123432", result.get(1).getPassword());
        assertEquals("13464367", result.get(1).getIdCardNo());
        assertEquals("Krzychu", result.get(1).getFirstName());
        assertEquals("Janusz", result.get(1).getLastName());
        assertEquals("987-654-321", result.get(1).getPhoneNumber());
        assertEquals(LocalDate.of(1910, 12, 11), result.get(0).getBirthday());
    }

    @Test
    void deletePatientByEmail_existingPatients_getPatients() {
        Patient patient = new Patient("dupa@gmail.com", "123", "1234567", "Tomek", "Nowak", "123-456-789", LocalDate.of(1910, 12, 11));

        when(patientRepository.findByEmail("dupa@gmail.com")).thenReturn(Optional.of(patient));

        patientService.deletePatientByEmail("dupa@gmail.com");

        verify(patientRepository, times(1)).delete(patient);
    }

    @Test
    void addNewPatient_addingPatients_shouldReturnNewPatientx2() { //przeanalizowac
        String email = "dupa@gmail.com";
        Patient patient = new Patient(email, "123", "1234567", "Tomek", "Nowak", "123-456-789", LocalDate.of(1910, 12, 11));

        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());

        Patient result = patientService.addNewPatient(patient);

        assertEquals("dupa@gmail.com", result.getEmail());
        assertEquals("123", result.getPassword());
        assertEquals("1234567", result.getIdCardNo());
        assertEquals("Tomek", result.getFirstName());
        assertEquals("Nowak", result.getLastName());
        assertEquals("123-456-789", result.getPhoneNumber());
        assertEquals(LocalDate.of(1910, 12, 11), result.getBirthday());
        verify(patientRepository, times(1)).save(patient);
    }

    @Test
    void editPatientByEmail_givenValidInput_shouldEditPatient() {
        Patient existingPatient = new Patient("dupa@gmail.com", "123", "1234567", "Tomek", "Nowak", "123-456-789", LocalDate.of(1910, 12, 11));
        Patient editedPatient = new Patient("dupa1@gmail.com", "123456", "1234567", "Romek", "Janusz", "462352432", LocalDate.of(1920, 10, 1));

        when(patientRepository.findByEmail("dupa@gmial.com")).thenReturn(Optional.of(existingPatient));

        Patient updates = patientService.editPatientByEmail("dupa@gmial.com", editedPatient);

        assertEquals("dupa1@gmail.com", updates.getEmail());
        assertEquals("123456", existingPatient.getPassword());
        assertEquals("Romek", existingPatient.getFirstName());
        assertEquals("Janusz", existingPatient.getLastName());
        assertEquals("462352432", existingPatient.getPhoneNumber());
    }

    @Test
    void updatePassword_existingPatient_shouldUpdatePassword() {
        String email = "dupa@gmail.com";
        String newPassword = "newpassword";
        Patient existingPatient = new Patient(email, "123", "1234567", "Tomek", "Nowak", "123-456-789", LocalDate.of(1910, 12, 11));

        when(patientRepository.findByEmail("dupa@gmail.com")).thenReturn(Optional.of(existingPatient));

        String updatedPassword = patientService.updatePassword(email, newPassword);

        assertEquals(newPassword, updatedPassword);
    }
}