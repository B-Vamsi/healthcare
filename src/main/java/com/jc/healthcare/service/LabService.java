package com.jc.healthcare.service;

import com.jc.healthcare.model.*;
import com.jc.healthcare.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class LabService {

    // ✅ Field Injection
    @Autowired
    private LabCategoryRepository categoryRepo;

    @Autowired
    private LabTestRepository testRepo;

    @Autowired
    private LabReportRepository reportRepo;

    // ========== CATEGORY CRUD ==========
    public List<LabCategory> getAllCategories() {
        return categoryRepo.findAll();
    }

    public LabCategory addCategory(LabCategory category) {
        return categoryRepo.save(category);
    }

    public LabCategory updateCategory(Long id, LabCategory updated) {
        LabCategory category = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setCategoryName(updated.getCategoryName());
        return categoryRepo.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepo.deleteById(id);
    }

    // ========== TEST CRUD ==========
    public List<LabTest> getAllTests() {
        return testRepo.findAll();
    }

    public LabTest addTest(LabTest test) {
        return testRepo.save(test);
    }

    // ✅ PATCH update (partial update)
    public LabTest patchUpdateTest(Long id, LabTest partialTest) {
        LabTest test = testRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Test not found"));

        if (partialTest.getTestName() != null)
            test.setTestName(partialTest.getTestName());

        if (partialTest.getTestCost() != null)
            test.setTestCost(partialTest.getTestCost());

        if (partialTest.getCategory() != null)
            test.setCategory(partialTest.getCategory());

        return testRepo.save(test);
    }

    public void deleteTest(Long id) {
        testRepo.deleteById(id);
    }

    // ========== REPORT CRUD ==========
    public LabReport uploadReport(Long patientId, Long testId, MultipartFile file) throws IOException {
        LabTest test = testRepo.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found"));

        LabReport report = new LabReport();
        report.setPatientId(patientId);
        report.setTest(test);
        report.setFileName(file.getOriginalFilename());
        report.setFileType(file.getContentType());
        report.setReportFile(file.getBytes());

        return reportRepo.save(report);
    }

    public List<LabReport> getReportsByPatient(Long patientId) {
        return reportRepo.findByPatientId(patientId);
    }

    public void deleteReport(Long reportId) {
        reportRepo.deleteById(reportId);
    }

    // ✅ PATCH update for file upload (only file update)
    public LabReport patchUpdateReportFile(Long reportId, MultipartFile newFile) throws IOException {
        LabReport report = reportRepo.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        if (newFile != null && !newFile.isEmpty()) {
            report.setFileName(newFile.getOriginalFilename());
            report.setFileType(newFile.getContentType());
            report.setReportFile(newFile.getBytes());
        }

        return reportRepo.save(report);
    }
}
