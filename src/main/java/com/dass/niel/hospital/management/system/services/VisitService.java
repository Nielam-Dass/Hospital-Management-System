package com.dass.niel.hospital.management.system.services;

import com.dass.niel.hospital.management.system.entities.Staff;
import com.dass.niel.hospital.management.system.entities.Visit;

import java.time.LocalDate;
import java.util.List;

public interface VisitService {
    public void addNewVisit(Visit visit);

    public void endVisit(Visit visit, LocalDate endDate);

    public void assignVisitToStaff(Visit visit, Staff staff);

    public long getNumOfActiveVisits();

    public List<Visit> getVisitsBySsnNameAndDateRange(Integer ssn, String firstName, String lastName, LocalDate fromDate, LocalDate toDate);

    public List<Visit> getActiveVisits();

    public List<Visit> getVisitsByStaffInvolved(Long staffId);

    public List<Visit> getActiveVisitsByStaffInvolved(Long staffId);

    public List<Visit> getAllVisits();

    public Visit getVisitById(Long visitId);
}
