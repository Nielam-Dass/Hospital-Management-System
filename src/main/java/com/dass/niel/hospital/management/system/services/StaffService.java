package com.dass.niel.hospital.management.system.services;

import com.dass.niel.hospital.management.system.entities.Staff;

import java.util.List;

public interface StaffService {
    void addNewStaff(Staff staff);

    long getNumOfStaff();

    List<Staff> getAllStaff();

    List<Staff> getStaffByFullNameAndDepartment(String firstName, String lastName, String department);

    List<Staff> getStaffByFullName(String firstName, String lastName);

    List<Staff> getStaffInDepartment(String department);

    Staff getStaffById(Long staffId);

    void updateStaff(Staff staff);
}
