package com.dass.niel.hospital.management.system.services;

import com.dass.niel.hospital.management.system.entities.Visit;
import com.dass.niel.hospital.management.system.repositories.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VisitService {

    @Autowired
    VisitRepository visitRepository;

    public void addNewVisit(Visit visit){
        visitRepository.save(visit);
    }

    public void endVisit(Visit visit, LocalDate endDate){
        visit.setDischargedOn(endDate);
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
