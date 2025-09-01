package com.dass.niel.hospital.management.system.services;

import com.dass.niel.hospital.management.system.entities.Staff;
import com.dass.niel.hospital.management.system.entities.Visit;
import com.dass.niel.hospital.management.system.repositories.VisitRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VisitServiceImpl implements VisitService {
    private final VisitRepository visitRepository;

    public VisitServiceImpl(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    public void addNewVisit(Visit visit){
        visitRepository.save(visit);
    }

    public void endVisit(Visit visit, LocalDate endDate){
        visit.setDischargedOn(endDate);
        visitRepository.save(visit);
    }

    public void assignVisitToStaff(Visit visit, Staff staff){
        List<Staff> staffInvolved = visit.getStaffInvolved();
        for(Staff s : staffInvolved){
            if(s.getStaffId().equals(staff.getStaffId())){
                return;
            }
        }
        staffInvolved.add(staff);
        visit.setStaffInvolved(staffInvolved);
        visitRepository.save(visit);
    }

    public long getNumOfActiveVisits(){
        return visitRepository.countByDischargedOnNull();
    }

    public List<Visit> getVisitsBySsnNameAndDateRange(Integer ssn, String firstName, String lastName, LocalDate fromDate, LocalDate toDate){
        return visitRepository.findBySsnNameAndAdmissionDateRange(ssn, firstName, lastName, fromDate, toDate);
    }

    public List<Visit> getActiveVisits(){
        return visitRepository.findByDischargedOnNull();
    }

    public List<Visit> getVisitsByStaffInvolved(Long staffId){
        return visitRepository.findByStaffInvolved(staffId);
    }

    public List<Visit> getActiveVisitsByStaffInvolved(Long staffId){
        return visitRepository.findActiveByStaffInvolved(staffId);
    }

    public List<Visit> getAllVisits(){
        return visitRepository.findAll();
    }

    public Visit getVisitById(Long visitId){
        Optional<Visit> visit = visitRepository.findById(visitId);
        if(visit.isPresent()){
            return visit.get();
        }
        else{
            return null;
        }
    }
}
