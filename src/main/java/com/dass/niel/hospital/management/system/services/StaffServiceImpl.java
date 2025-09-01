package com.dass.niel.hospital.management.system.services;

import com.dass.niel.hospital.management.system.entities.Staff;
import com.dass.niel.hospital.management.system.repositories.StaffRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffServiceImpl implements StaffService {
    private final StaffRepository staffRepository;

    public StaffServiceImpl(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

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

    public Staff getStaffById(Long staffId) {
        Optional<Staff> staff = staffRepository.findById(staffId);
        if(staff.isPresent()){
            return staff.get();
        }
        else{
            return null;
        }
    }

    public void updateStaff(Staff staff){
        staffRepository.save(staff);
    }
}
