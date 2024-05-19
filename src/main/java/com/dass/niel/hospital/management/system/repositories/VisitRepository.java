package com.dass.niel.hospital.management.system.repositories;

import com.dass.niel.hospital.management.system.entities.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

    @Query("select v from Visit v inner join v.patient p where (p.ssn = :ssn or :ssn is null) and " +
            "(p.firstName = :firstName or :firstName is null) and (p.lastName = :lastName or :lastName is null) and " +
            "(v.admittedOn >= :fromDate or :fromDate is null) and (v.admittedOn <= :toDate or :toDate is null)")
    List<Visit> findBySsnNameAndAdmissionDateRange(Integer ssn, String firstName, String lastName, LocalDate fromDate, LocalDate toDate);

    @Query("select v from Visit v inner join v.staffInvolved s where s.staffId = :staffId")
    List<Visit> findByStaffInvolved(Long staffId);

    @Query("select v from Visit v inner join v.staffInvolved s where s.staffId = :staffId and v.dischargedOn is null")
    List<Visit> findActiveByStaffInvolved(Long staffId);

    List<Visit> findByDischargedOnNull();

    Long countByDischargedOnNull();

}
