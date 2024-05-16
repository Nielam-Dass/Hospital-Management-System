package com.dass.niel.hospital.management.system.services;

import com.dass.niel.hospital.management.system.entities.Patient;
import com.dass.niel.hospital.management.system.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    @Autowired
    PatientRepository patientRepository;

    public Patient addNewPatient(Patient patient){
        return patientRepository.save(patient);
    }

    public long getNumOfPatients(){
        return patientRepository.count();
    }

    public List<Patient> getAllPatients(){
        return patientRepository.findAll();
    }

    public Patient getPatientBySsn(Integer ssn){
        return patientRepository.findBySsn(ssn);
    }

    public List<Patient> getPatientsByFullName(String firstName, String lastName){
        return patientRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    public Patient getPatientById(Long patientId){
        Optional<Patient> patient = patientRepository.findById(patientId);
        if(patient.isPresent()){
            return patient.get();
        }
        else{
            return null;
        }
    }

    public void updatePatient(Patient patient){
        patientRepository.save(patient);
    }
}
