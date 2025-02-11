package com.dass.niel.hospital.management.system.services;

import com.dass.niel.hospital.management.system.entities.MedicalRecord;
import com.dass.niel.hospital.management.system.repositories.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService{
    @Autowired
    MedicalRecordRepository medicalRecordRepository;

    @Value("${file_store_folder}") String folderName;

    Path fileStore;

    public void init(){
        fileStore = Paths.get(folderName);

        try{
            if(!Files.exists(fileStore)){
                Files.createDirectories(fileStore);
            }
        } catch (IOException e) {
            System.out.println("Could not create " + folderName + " folder");
            throw new RuntimeException(e);
        }
    }

    public MedicalRecord addNewMedRecord(MedicalRecord medicalRecord, MultipartFile file){
        if(!file.isEmpty()){
            Path recordFilePath = storeMedRecordFile(file);
            medicalRecord.setRecordFile(recordFilePath.toString());
        }
        else if(medicalRecord.getRecordFile()!=null){
            throw new RuntimeException("Record file name was provided without file");
        }
        return medicalRecordRepository.save(medicalRecord);
    }

    public List<MedicalRecord> getMedRecordsBySsnNameAndKeyword(Integer ssn, String firstName, String lastName, String keyword){
        return medicalRecordRepository.findBySsnNameAndKeyword(ssn, firstName, lastName, keyword);
    }

    public Long getNumOfMedRecords(){
        return medicalRecordRepository.count();
    }

    public List<MedicalRecord> getAllMedRecords(){
        return medicalRecordRepository.findAll();
    }

    public MedicalRecord getMedRecordById(Long recordId){
        Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findById(recordId);
        if(medicalRecord.isPresent()){
            return medicalRecord.get();
        }
        else{
            return null;
        }
    }

    private Path storeMedRecordFile(MultipartFile file){
        try{
            Path dest = fileStore.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), dest);
            return dest;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Resource loadMedRecordFile(Long recordId){
        MedicalRecord medicalRecord = getMedRecordById(recordId);
        if(medicalRecord==null)
            return null;

        String recordFile = medicalRecord.getRecordFile();
        if(recordFile==null)
            return null;

        Path recordFilePath = Paths.get(recordFile);
        try {
            Resource recordResource = new UrlResource(recordFilePath.toUri());
            if(recordResource.exists() || recordResource.isReadable()){
                return recordResource;
            }
            else{
                throw new RuntimeException("Could not read record file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
