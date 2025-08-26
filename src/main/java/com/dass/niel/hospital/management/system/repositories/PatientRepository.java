package com.dass.niel.hospital.management.system.repositories;

import com.dass.niel.hospital.management.system.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PatientRepository extends JpaRepository<Patient, Long> {
    Patient findBySsn(Integer ssn);

    List<Patient> findByFirstNameAndLastName(String firstName, String lastName);

}
