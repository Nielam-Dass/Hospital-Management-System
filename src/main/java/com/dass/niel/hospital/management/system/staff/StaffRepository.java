package com.dass.niel.hospital.management.system.staff;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    List<Staff> findByFirstNameAndLastName(String firstName, String lastName);

    List<Staff> findByDepartment(String department);

    List<Staff> findByFirstNameAndLastNameAndDepartment(String firstName, String lastName, String department);

}
