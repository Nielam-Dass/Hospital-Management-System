package com.dass.niel.hospital.management.system.repositories;

import com.dass.niel.hospital.management.system.entities.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Long> {

    @Query("select v from visit v inner join v.patient p where (p.ssn = :ssn or :ssn is null) and " +
            "(p.firstName = :firstName or :firstName is null) and (p.lastName = :lastName or :lastName is null) and " +
            "(v.admittedOn >= :fromDate or :fromDate is null) and (v.admittedOn <= :toDate or :toDate is null)")
    List<Visit> findBySsnNameAndAdmissionDateRange(Integer ssn, String firstName, String lastName, LocalDate fromDate, LocalDate toDate);

    List<Visit> findByDischargedOnNull();

    Long countByDischargedOnNull();

}
