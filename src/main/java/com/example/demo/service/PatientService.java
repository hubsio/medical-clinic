package com.example.demo.service;


import com.example.demo.exception.IllegalUserIdChangeException;
import com.example.demo.exception.InvalidEmailException;
import com.example.demo.exception.PatientNotFoundException;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.model.Patient;
import com.example.demo.model.dto.PatientDTO;
import com.example.demo.model.mapper.PatientMapper;
import com.example.demo.repository.PatientRepository;
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

    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(patientMapper::patientToPatientDTO)
                .collect(Collectors.toList());
    }

    public PatientDTO getPatientByEmail(String email) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient with the provided email does not exist"));
        return patientMapper.patientToPatientDTO(patient);
    }

    @Transactional
    public PatientDTO addNewPatient(Patient patient) {
        String email = patient.getEmail();
        if (email == null || email.isEmpty()) {
            throw new InvalidEmailException("Invalid email address");
        }
        Optional<Patient> existingPatient = patientRepository.findByEmail(email);
        if (existingPatient.isPresent()) {
            throw new UserAlreadyExistsException("User with the provided email already exists");
        }

        patientRepository.save(patient);
        return patientMapper.patientToPatientDTO(patient);
    }

    @Transactional
    public void deletePatientByEmail(String email) {
        Patient patientToDelete = patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient with the provided email does not exist"));

        patientRepository.delete(patientToDelete);
    }

    @Transactional
    public PatientDTO editPatientByEmail(String email, Patient editedPatient) {
        Patient existingPatient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient with given email does not exist"));

        if (!existingPatient.getIdCardNo().equals(editedPatient.getIdCardNo())) {
            throw new IllegalUserIdChangeException("Changing ID number is not allowed");
        }

        if (editedPatient.getFirstName() == null ||
                editedPatient.getLastName() == null ||
                editedPatient.getPassword() == null ||
                editedPatient.getPhoneNumber() == null) {
            throw new IllegalArgumentException("Patient data cannot be null");
        }

        existingPatient.setFirstName(editedPatient.getFirstName());
        existingPatient.setLastName(editedPatient.getLastName());
        existingPatient.setPassword(editedPatient.getPassword());
        existingPatient.setPhoneNumber(editedPatient.getPhoneNumber());

        patientRepository.save(existingPatient);
        return patientMapper.patientToPatientDTO(existingPatient);
    }

    @Transactional
    public String updatePassword(@PathVariable String email, @RequestBody String newPassword) {
        Patient existingPatient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient with given email does not exist"));

        if (newPassword == null || newPassword.isEmpty()) {
            throw new InvalidEmailException("New password cannot be empty");
        }
        existingPatient.setPassword(newPassword);

        patientRepository.save(existingPatient);
        return newPassword;
    }
}

