package com.example.demo.service;


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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final UserRepository userRepository;

    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(patientMapper::patientToPatientDTO)
                .collect(Collectors.toList());
    }

    public PatientDTO getPatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient with the provided ID does not exist"));
        return patientMapper.patientToPatientDTO(patient);
    }

    @Transactional
    public PatientDTO addNewPatient(CreatePatientCommandDTO command) {
        String email = command.getEmail();
        if (email == null || email.isEmpty()) {
            throw new InvalidEmailException("Invalid email address");
        }
        Optional<User> existingPatient = userRepository.findByEmail(email);
        if (existingPatient.isPresent()) {
            throw new UserAlreadyExistsException("User with the provided email already exists");
        }
        Patient patient = Patient.create(command);
        patientRepository.save(patient);
        return patientMapper.patientToPatientDTO(patient);
    }

    @Transactional
    public void deletePatient(Long id) {
        Patient patientToDelete = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient with the provided ID does not exist"));

        patientRepository.delete(patientToDelete);
    }

    @Transactional
    public PatientDTO editPatientById(Long id, EditPatientCommandDTO editedPatient) {
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient with given ID does not exist"));

        if (editedPatient.getFirstName() == null ||
                editedPatient.getLastName() == null ||
                editedPatient.getPassword() == null ||
                editedPatient.getPhoneNumber() == null) {
            throw new IllegalArgumentException("Patient data cannot be null");
        }

        existingPatient.setFirstName(editedPatient.getFirstName());
        existingPatient.setLastName(editedPatient.getLastName());
        existingPatient.getUser().setPassword(editedPatient.getPassword());
        existingPatient.setPhoneNumber(editedPatient.getPhoneNumber());

        patientRepository.save(existingPatient);
        return patientMapper.patientToPatientDTO(existingPatient);
    }

    @Transactional
    public String updatePassword(@PathVariable Long id, @RequestBody String newPassword) {
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient with given ID does not exist"));

        if (newPassword == null || newPassword.isEmpty()) {
            throw new InvalidEmailException("New password cannot be empty");
        }
        existingPatient.getUser().setPassword(newPassword);

        patientRepository.save(existingPatient);
        return newPassword;
    }
}

