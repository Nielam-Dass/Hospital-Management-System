package com.dass.niel.hospital.management.system.services;

import com.dass.niel.hospital.management.system.entities.Patient;

import java.util.List;

public interface PatientService {

    public Patient addNewPatient(Patient patient);

    public long getNumOfPatients();

    public List<Patient> getAllPatients();

    public Patient getPatientBySsn(Integer ssn);

    public List<Patient> getPatientsByFullName(String firstName, String lastName);

    public Patient getPatientById(Long patientId);

    public void updatePatient(Patient patient);
}
