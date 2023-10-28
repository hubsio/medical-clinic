package com.example.demo.service;

import com.example.demo.exception.DoctorNotFoundException;
import com.example.demo.exception.InvalidEmailException;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.model.dto.CreateDoctorCommandDTO;
import com.example.demo.model.dto.DoctorDTO;
import com.example.demo.model.dto.EditDoctorCommandDTO;
import com.example.demo.model.entity.Doctor;
import com.example.demo.model.entity.User;
import com.example.demo.model.mapper.DoctorMapper;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DoctorServiceTest {
    DoctorRepository doctorRepository;
    UserRepository userRepository;
    DoctorService doctorService;
    DoctorMapper doctorMapper;
    @BeforeEach
    void setUp() {
        this.doctorRepository = Mockito.mock(DoctorRepository.class);
        this.userRepository = Mockito.mock(UserRepository.class);
        this.doctorMapper = Mappers.getMapper(DoctorMapper.class);
        this.doctorService = new DoctorService(doctorRepository, doctorMapper, userRepository);
    }

    @Test
    void getDoctor_existingDoctor_shouldReturnDoctor() {
        Doctor doctor = new Doctor(1L, "John", "Doe", "123-456-789", LocalDate.of(1990,12,12), "Cardiology", null, null);

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        DoctorDTO expectedDoctorDTO = doctorMapper.doctorToDoctorDTO(doctor);

        DoctorDTO actualDoctor = doctorService.getDoctor(1L);
        assertEquals(expectedDoctorDTO, actualDoctor);
    }

    @Test
    void getAllDoctors_existingDoctors_shouldReturnDoctors() {
        List<Doctor> expectedDoctors = new ArrayList<>();
        expectedDoctors.add(new Doctor(1L, "John", "Doe", "123-456-789", LocalDate.of(1990,12,12), "Cardiology", null, null));
        expectedDoctors.add(new Doctor(1L, "Hubert", "Nowak", "123456789", LocalDate.of(1990,12,12), "Dermatology", null, null));

        when(doctorRepository.findAll()).thenReturn(expectedDoctors);

        List<DoctorDTO> result = doctorService.getAllDoctors();

        assertEquals(2, result.size());

        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Doe", result.get(0).getLastName());
        assertEquals("123-456-789", result.get(0).getPhoneNumber());
        assertEquals("Cardiology", result.get(0).getSpecialization());

        assertEquals("Hubert", result.get(1).getFirstName());
        assertEquals("Nowak", result.get(1).getLastName());
        assertEquals("123456789", result.get(1).getPhoneNumber());
        assertEquals("Dermatology", result.get(1).getSpecialization());
    }

    @Test
    void deleteDoctor_existingDoctors_getDoctors() {
        Doctor doctor = new Doctor(1L, "John", "Doe", "123-456-789", LocalDate.of(1990,12,12), "Cardiology", null, null);

        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));

        doctorService.deleteDoctor(doctor.getId());

        verify(doctorRepository, times(1)).delete(doctor);
    }

    @Test
    void addNewDoctor_validData_shouldReturnNewDoctorDTO() {
        CreateDoctorCommandDTO commandDTO = new CreateDoctorCommandDTO("12345543211", "Hubert", "Nowak", "123456789", LocalDate.of(1990,11,5),"Doctor123", "doctro123@gmail.com", "12345", "Cardiology");

        when(userRepository.findByEmail(commandDTO.getEmail())).thenReturn(Optional.empty());

        DoctorDTO resultDTO = doctorService.addNewDoctor(commandDTO);
        resultDTO.setEmail("doctro123@gmail.com");
        resultDTO.setFirstName("Hubert");
        resultDTO.setLastName("Nowak");
        resultDTO.setSpecialization("Cardiology");

        assertEquals("doctro123@gmail.com", resultDTO.getEmail());
        assertEquals("Hubert", resultDTO.getFirstName());
        assertEquals("Nowak", resultDTO.getLastName());
        assertEquals("Cardiology", resultDTO.getSpecialization());
        verify(doctorRepository, times(1)).save(any());
    }

    @Test
    void editDoctorById_existingDoctor_shouldEditDoctor() {
        long doctorId = 1L;
        Doctor existingDoctor = new Doctor(doctorId, "John", "Doe", "123-456-789", LocalDate.of(1990,12,12), "Cardiology", new User(), null);
        EditDoctorCommandDTO editedDoctor = new EditDoctorCommandDTO("NewFirstName", "NewLastName", "NewPassword", "NewPhoneNumber", "NewSpecialization");

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(existingDoctor));

        DoctorDTO updatedDoctorDTO = doctorService.editDoctorById(doctorId, editedDoctor);

        assertEquals("NewFirstName", updatedDoctorDTO.getFirstName());
        assertEquals("NewLastName", updatedDoctorDTO.getLastName());
        assertEquals("NewSpecialization", updatedDoctorDTO.getSpecialization());
        verify(doctorRepository, times(1)).save(existingDoctor);
    }

    @Test
    void updatePassword_existingDoctor_shouldUpdatePassword() {
        Long doctorId = 1L;
        String newPassword = "newpassword";
        Doctor existingDoctor = new Doctor();
        existingDoctor.setId(doctorId);
        existingDoctor.setUser(new User());

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(existingDoctor));

        String result = doctorService.updatePassword(doctorId, newPassword);

        assertEquals(newPassword, result);
    }

    @Test
    void getDoctor_nonexistentDoctor_shouldThrowException() {
        Long nonExistentDoctorId = 100L;

        when(doctorRepository.findById(nonExistentDoctorId)).thenReturn(Optional.empty());

        DoctorNotFoundException exception = assertThrows(DoctorNotFoundException.class, () -> doctorService.getDoctor(nonExistentDoctorId));

        assertEquals("Doctor with the provided ID does not exist", exception.getMessage());
    }

    @Test
    void addNewDoctor_emptyEmail_shouldThrowException() {
        CreateDoctorCommandDTO doctor = new CreateDoctorCommandDTO("12345543211", "Hubert", "Nowak", "123456789", LocalDate.of(1990,11,5),"Doctor123", null, "12345", "Cardiology");

        InvalidEmailException exception = assertThrows(InvalidEmailException.class, () -> doctorService.addNewDoctor(doctor));

        assertEquals("Invalid email address", exception.getMessage());
    }

    @Test
    void addNewDoctor_existingEmail_shouldThrowException() {
        CreateDoctorCommandDTO createDoctorCommandDTO = new CreateDoctorCommandDTO("123", "Hubert", "Nowak", "987-654-321", LocalDate.of(1990,12,12), "hubi123", "hubi123@gmail.com", "123321", "Cardiologist");
        User user = new User(1L, "Jarek", "jarek123@gmail.com", "1321", null, null);

        when(userRepository.findByEmail("hubi123@gmail.com")).thenReturn(Optional.of(user));

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> doctorService.addNewDoctor(createDoctorCommandDTO));

        assertEquals("User with the provided email already exists", exception.getMessage());
    }

    @Test
    void deleteDoctor_doctorNotFound_shouldThrowException() {
        Long nonExistentDoctorId = 100L;

        when(doctorRepository.findById(nonExistentDoctorId)).thenReturn(Optional.empty());

        DoctorNotFoundException exception = assertThrows(DoctorNotFoundException.class, () -> doctorService.deleteDoctor(nonExistentDoctorId));

        assertEquals("Doctor with the provided ID does not exist", exception.getMessage());
    }

    @Test
    void editDoctorById_doctorNotFound_shouldThrowException() {
        EditDoctorCommandDTO editedDoctor = new EditDoctorCommandDTO("NewFirstName", "NewLastName", "NewPhoneNumber", "NewPassword", "NewSpecialization");

        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        DoctorNotFoundException exception = assertThrows(DoctorNotFoundException.class, () -> doctorService.editDoctorById(1L, editedDoctor));

        assertEquals("Doctor with given ID does not exist", exception.getMessage());
    }

    @Test
    void editDoctorById_nullFields_shouldThrowException() {
        Doctor existingDoctor = new Doctor();
        existingDoctor.setId(1L);
        EditDoctorCommandDTO editedDoctor = new EditDoctorCommandDTO(null, null, null, null, null);

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(existingDoctor));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> doctorService.editDoctorById(1L, editedDoctor));

        assertEquals("Data cannot be null", exception.getMessage());
    }

    @Test
    void updatePassword_doctorNotFound_shouldThrowException() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        DoctorNotFoundException exception = assertThrows(DoctorNotFoundException.class, () -> doctorService.updatePassword(1L, "newpassword"));

        assertEquals("Doctor with given ID does not exist", exception.getMessage());
    }

    @Test
    void updatePassword_nullPassword_shouldThrowException() {
        Doctor existingDoctor = new Doctor();
        existingDoctor.setId(1L);
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(existingDoctor));

        InvalidEmailException exception = assertThrows(InvalidEmailException.class, () -> doctorService.updatePassword(1L, null));

        assertEquals("New password cannot be empty", exception.getMessage());
    }

    @Test
    void updatePassword_emptyPassword_shouldThrowException() {
        Doctor existingDoctor = new Doctor();
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(existingDoctor));

        InvalidEmailException exception = assertThrows(InvalidEmailException.class, () -> doctorService.updatePassword(1L, ""));

        assertEquals("New password cannot be empty", exception.getMessage());
    }
}
