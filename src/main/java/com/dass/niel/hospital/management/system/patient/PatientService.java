package com.dass.niel.hospital.management.system.patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    @Autowired
    PatientRepository patientRepository;

    public void addNewPatient(Patient patient){
        patientRepository.save(patient);
    }

    public long getNumOfPatients(){
        return patientRepository.count();
    }

    public List<Patient> getAllPatients(){
        return patientRepository.findAll();
    }
}
