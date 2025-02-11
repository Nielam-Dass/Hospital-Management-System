package com.dass.niel.hospital.management.system.services;

import com.dass.niel.hospital.management.system.entities.Staff;

import java.util.List;

public interface StaffService {
    public void addNewStaff(Staff staff);

    public long getNumOfStaff();

    public List<Staff> getAllStaff();

    public List<Staff> getStaffByFullNameAndDepartment(String firstName, String lastName, String department);

    public List<Staff> getStaffByFullName(String firstName, String lastName);

    public List<Staff> getStaffInDepartment(String department);

    public Staff getStaffById(Long staffId);

    public void updateStaff(Staff staff);
}
