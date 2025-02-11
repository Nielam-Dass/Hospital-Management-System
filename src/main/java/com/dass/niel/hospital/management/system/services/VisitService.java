package com.dass.niel.hospital.management.system.services;

import com.dass.niel.hospital.management.system.entities.Staff;
import com.dass.niel.hospital.management.system.entities.Visit;

import java.time.LocalDate;
import java.util.List;

public interface VisitService {
    void addNewVisit(Visit visit);

    void endVisit(Visit visit, LocalDate endDate);

    void assignVisitToStaff(Visit visit, Staff staff);

    long getNumOfActiveVisits();

    List<Visit> getVisitsBySsnNameAndDateRange(Integer ssn, String firstName, String lastName, LocalDate fromDate, LocalDate toDate);

    List<Visit> getActiveVisits();

    List<Visit> getVisitsByStaffInvolved(Long staffId);

    List<Visit> getActiveVisitsByStaffInvolved(Long staffId);

    List<Visit> getAllVisits();

    Visit getVisitById(Long visitId);
}
