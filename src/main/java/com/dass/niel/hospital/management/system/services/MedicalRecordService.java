package com.dass.niel.hospital.management.system.services;

import com.dass.niel.hospital.management.system.entities.MedicalRecord;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MedicalRecordService {
    public void init();

    public MedicalRecord addNewMedRecord(MedicalRecord medicalRecord, MultipartFile file);

    public List<MedicalRecord> getMedRecordsBySsnNameAndKeyword(Integer ssn, String firstName, String lastName, String keyword);

    public Long getNumOfMedRecords();

    public List<MedicalRecord> getAllMedRecords();

    public MedicalRecord getMedRecordById(Long recordId);

    public Resource loadMedRecordFile(Long recordId);
}
