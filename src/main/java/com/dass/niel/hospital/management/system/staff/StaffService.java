package com.dass.niel.hospital.management.system.staff;

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

}
