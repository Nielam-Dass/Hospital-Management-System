package com.dass.niel.hospital.management.system.services;

import com.dass.niel.hospital.management.system.entities.Patient;

import java.util.List;

public interface PatientService {
    Patient addNewPatient(Patient patient);

    long getNumOfPatients();

    List<Patient> getAllPatients();

    Patient getPatientBySsn(Integer ssn);

    List<Patient> getPatientsByFullName(String firstName, String lastName);

    Patient getPatientById(Long patientId);

    void updatePatient(Patient patient);
}
