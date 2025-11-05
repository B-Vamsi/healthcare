package com.jc.healthcare.service;

import com.jc.healthcare.model.Doctor;
import com.jc.healthcare.model.LoginDetails;
import com.jc.healthcare.repository.DoctorRepository;
import com.jc.healthcare.repository.LoginDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private LoginDetailsRepository loginRepository;

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }
    public long Count() {
        return doctorRepository.getDoctorCount();
    }

    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    // 🔽 Modified createDoctor() method
    public Doctor createDoctor(Doctor doctor) {
        // Validation for duplicate entries
        if (doctor.getEmail() != null && doctorRepository.existsByEmail(doctor.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (doctor.getPhone() != null && doctorRepository.existsByPhone(doctor.getPhone())) {
            throw new RuntimeException("Phone number already exists");
        }
        if (doctor.getMedicalLicenseNo() != null && doctorRepository.existsByMedicalLicenseNo(doctor.getMedicalLicenseNo())) {
            throw new RuntimeException("Medical license number already exists");
        }
        if (doctor.getStatus() == null || doctor.getStatus().isEmpty()) {
            doctor.setStatus("ACTIVE");
        }

        // Step 1: Save Doctor
        Doctor savedDoctor = doctorRepository.save(doctor);

        if (savedDoctor.getEmail() != null && savedDoctor.getPassword() != null) {
            if (!loginRepository.existsByEmail(savedDoctor.getEmail())) {
                LoginDetails login = new LoginDetails();
                login.setEmail(savedDoctor.getEmail());
                login.setPassword(savedDoctor.getPassword());
                login.setRole(savedDoctor.getRole() != null ? savedDoctor.getRole() : "DOCTOR");
               
            } else {
                throw new RuntimeException("Email already exists in login_details table");
            }
        }

        return savedDoctor;
    }


    public Doctor updateDoctor(Long id, Doctor doctorDetails) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));

        if (doctorDetails.getEmail() != null &&
                !doctorDetails.getEmail().equals(doctor.getEmail()) &&
                doctorRepository.existsByEmail(doctorDetails.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (doctorDetails.getPhone() != null &&
                !doctorDetails.getPhone().equals(doctor.getPhone()) &&
                doctorRepository.existsByPhone(doctorDetails.getPhone())) {
            throw new RuntimeException("Phone already exists");
        }

        if (doctorDetails.getMedicalLicenseNo() != null &&
                !doctorDetails.getMedicalLicenseNo().equals(doctor.getMedicalLicenseNo()) &&
                doctorRepository.existsByMedicalLicenseNo(doctorDetails.getMedicalLicenseNo())) {
            throw new RuntimeException("Medical license number already exists");
        }

        doctor.setDoctorName(doctorDetails.getDoctorName());
        doctor.setSpecialization(doctorDetails.getSpecialization());
        doctor.setPhone(doctorDetails.getPhone());
        doctor.setEmail(doctorDetails.getEmail());
        doctor.setExperience(doctorDetails.getExperience());
        doctor.setStatus(doctorDetails.getStatus());
        doctor.setAddress(doctorDetails.getAddress());
        doctor.setDateOfBirth(doctorDetails.getDateOfBirth());
        doctor.setGender(doctorDetails.getGender());
        doctor.setCity(doctorDetails.getCity());
        doctor.setState(doctorDetails.getState());
        doctor.setPinCode(doctorDetails.getPinCode());
        doctor.setCountry(doctorDetails.getCountry());
        doctor.setMedicalLicenseNo(doctorDetails.getMedicalLicenseNo());
        doctor.setRole(doctorDetails.getRole());
        doctor.setImage(doctorDetails.getImage());
        doctor.setPassword(doctorDetails.getPassword());
      
        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
        doctorRepository.delete(doctor);
    }

    public List<Doctor> getActiveDoctors() {
        return doctorRepository.findByStatus("ACTIVE");
    }

    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization);
    }

    public Doctor changeDoctorStatus(Long id, String status) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
        doctor.setStatus(status);
        return doctorRepository.save(doctor);
    }
}
