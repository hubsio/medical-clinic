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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final UserRepository userRepository;

    public List<DoctorDTO> getAllDoctors() {
        System.out.println("Hi");
        log.info("Getting all doctors");
        return doctorRepository.findAll().stream()
                .map(doctorMapper::doctorToDoctorDTO)
                .collect(Collectors.toList());
    }

    public DoctorDTO getDoctor(Long id) {
        log.info("Getting doctor with ID: {}", id);
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor with the provided ID does not exist"));
        log.info("Doctor with ID {} found", id);
        return doctorMapper.doctorToDoctorDTO(doctor);
    }

    @Transactional
    public DoctorDTO addNewDoctor(CreateDoctorCommandDTO command) {
        log.info("Add new doctor {}", command.toString());
        String email = command.getEmail();
        if (email == null || email.isEmpty()) {
            throw new InvalidEmailException("Invalid email address");
        }
        Optional<User> existingDoctor = userRepository.findByEmail(email);
        if (existingDoctor.isPresent()) {
            throw new UserAlreadyExistsException("User with the provided email already exists");
        }
        Doctor doctor = Doctor.create(command);
        doctorRepository.save(doctor);
        log.info("New doctor added with ID: {}", doctor.getId());
        return doctorMapper.doctorToDoctorDTO(doctor);
    }

    @Transactional
    public void deleteDoctor(Long id) {
        log.info("Deleting doctor with ID: {}", id);
        Doctor doctorToDelete = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor with the provided ID does not exist"));

        doctorRepository.delete(doctorToDelete);
    }

    @Transactional
    public DoctorDTO editDoctorById(Long id, EditDoctorCommandDTO editedDoctor) {
        log.info("Editing doctor with ID: {}", id);
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor with given ID does not exist"));

        if (editedDoctor.getFirstName() == null ||
                editedDoctor.getLastName() == null ||
                editedDoctor.getPassword() == null ||
                editedDoctor.getSpecialization() == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }

        existingDoctor.setFirstName(editedDoctor.getFirstName());
        existingDoctor.setLastName(editedDoctor.getLastName());
        existingDoctor.getUser().setPassword(editedDoctor.getPassword());
        existingDoctor.setSpecialization(editedDoctor.getSpecialization());

        doctorRepository.save(existingDoctor);
        return doctorMapper.doctorToDoctorDTO(existingDoctor);
    }

    @Transactional
    public String updatePassword(Long id, String newPassword) {
        log.info("Updating password for doctor with ID: {}", id);
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor with given ID does not exist"));

        if (newPassword == null || newPassword.isEmpty()) {
            throw new InvalidEmailException("New password cannot be empty");
        }
        existingDoctor.getUser().setPassword(newPassword);

        doctorRepository.save(existingDoctor);
        log.info("Password updated for doctor with ID: {}", id);
        return newPassword;
    }
}
