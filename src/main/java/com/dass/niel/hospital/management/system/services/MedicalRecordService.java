package com.dass.niel.hospital.management.system.services;

import com.dass.niel.hospital.management.system.entities.MedicalRecord;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MedicalRecordService {
    void init();

    MedicalRecord addNewMedRecord(MedicalRecord medicalRecord, MultipartFile file);

    List<MedicalRecord> getMedRecordsBySsnNameAndKeyword(Integer ssn, String firstName, String lastName, String keyword);

    Long getNumOfMedRecords();

    List<MedicalRecord> getAllMedRecords();

    MedicalRecord getMedRecordById(Long recordId);

    Resource loadMedRecordFile(Long recordId);
}
