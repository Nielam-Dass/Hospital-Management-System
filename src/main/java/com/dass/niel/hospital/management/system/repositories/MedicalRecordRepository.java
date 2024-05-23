package com.dass.niel.hospital.management.system.repositories;

import com.dass.niel.hospital.management.system.entities.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    @Query("select r from MedicalRecord r inner join r.patient p inner join r.addedBy where (p.ssn = :ssn or :ssn is null) and " +
            "(p.firstName = :firstName or :firstName is null) and (p.lastName = :lastName or :lastName is null) and " +
            "((r.name like %:keyword%) or (r.description like %:keyword%) or (:keyword is null))")
    List<MedicalRecord> findBySsnNameAndKeyword(Integer ssn, String firstName, String lastName, String keyword);
}
