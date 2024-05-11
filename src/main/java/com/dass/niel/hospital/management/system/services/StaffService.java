package com.dass.niel.hospital.management.system.services;

import com.dass.niel.hospital.management.system.repositories.StaffRepository;
import com.dass.niel.hospital.management.system.entities.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffService {

    @Autowired
    StaffRepository staffRepository;

    public void addNewStaff(Staff staff){
        staffRepository.save(staff);
    }

    public long getNumOfStaff(){
        return staffRepository.count();
    }

    public List<Staff> getAllStaff(){
        return staffRepository.findAll();
    }

    public List<Staff> getStaffByFullNameAndDepartment(String firstName, String lastName, String department){
        return staffRepository.findByFirstNameAndLastNameAndDepartment(firstName, lastName, department);
    }

    public List<Staff> getStaffByFullName(String firstName, String lastName){
        return staffRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    public List<Staff> getStaffInDepartment(String department){
        return staffRepository.findByDepartment(department);
    }

}
