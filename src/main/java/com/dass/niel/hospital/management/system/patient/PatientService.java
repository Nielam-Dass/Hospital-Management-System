package com.dass.niel.hospital.management.system.patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
}
