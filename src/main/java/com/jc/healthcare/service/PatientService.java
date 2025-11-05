package com.jc.healthcare.service;

import com.jc.healthcare.model.Patient;
import com.jc.healthcare.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public Patient addPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public long Count() {
        return patientRepository.getPatientCount();
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    public Patient updatePatient(Long id, Patient updatedPatient) {
        return patientRepository.findById(id).map(patient -> {
            patient.setName(updatedPatient.getName());
            patient.setGender(updatedPatient.getGender());
            patient.setAadhar(updatedPatient.getAadhar());
            patient.setPhone(updatedPatient.getPhone());
            patient.setDateOfBirth(updatedPatient.getDateOfBirth());
            patient.setAddress(updatedPatient.getAddress());
            patient.setDoctorId(updatedPatient.getDoctorId());
            patient.setAppointmentDate(updatedPatient.getAppointmentDate());
            patient.setAppointmentTime(updatedPatient.getAppointmentTime());

            patient.setDosageInstructions(updatedPatient.getDosageInstructions());
            patient.setGeneratedAt(updatedPatient.getGeneratedAt());
            patient.setNotes(updatedPatient.getNotes());
            patient.setSelectedMedicines(updatedPatient.getSelectedMedicines());
            patient.setDateIssued(updatedPatient.getDateIssued());
            patient.setMedication(updatedPatient.getMedication());
            patient.setSelectedTests(updatedPatient.getSelectedTests());
            patient.setMedisionStatus(updatedPatient.getMedisionStatus());
            patient.setDoctorStatus(updatedPatient.getDoctorStatus());
            patient.setLabStatus(updatedPatient.getLabStatus());
            return patientRepository.save(patient);
        }).orElseThrow(() -> new RuntimeException("Patient not found with ID: " + id));
    }

    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new RuntimeException("Patient not found with ID: " + id);
        }
        patientRepository.deleteById(id);
    }

    public List<Patient> getByMedisionStatus(String status) {
        return patientRepository.findByMedisionStatusIgnoreCase(status);
    }

    public List<Patient> getByDoctorStatus(String status) {
        return patientRepository.findByDoctorStatusIgnoreCase(status);
    }

    public List<Patient> getByLabStatus(String status) {
        return patientRepository.findByLabStatusIgnoreCase(status);
    }

    // ✅ Filter Patients by Date Range + Status
    public List<Patient> getFilteredPatientsNative(String fromDate, String medisionStatus, String doctorStatus, Long doctorId) {
        try {
            return patientRepository.findPatientsByStatusAndDateNative(fromDate, medisionStatus, doctorStatus, doctorId);
        } catch (Exception e) {
            throw new RuntimeException("❌ Error while fetching data: " + e.getMessage());
        }
    }

}
