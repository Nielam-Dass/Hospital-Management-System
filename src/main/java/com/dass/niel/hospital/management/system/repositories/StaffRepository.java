package com.dass.niel.hospital.management.system.repositories;

import com.dass.niel.hospital.management.system.entities.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    List<Staff> findByFirstNameAndLastName(String firstName, String lastName);

    List<Staff> findByDepartment(String department);

    List<Staff> findByFirstNameAndLastNameAndDepartment(String firstName, String lastName, String department);

}
