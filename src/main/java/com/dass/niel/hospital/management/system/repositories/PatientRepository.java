package com.dass.niel.hospital.management.system.repositories;

import com.dass.niel.hospital.management.system.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Patient findBySsn(Integer ssn);

    List<Patient> findByFirstNameAndLastName(String firstName, String lastName);

}
