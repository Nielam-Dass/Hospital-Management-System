package com.dass.niel.hospital.management.system;

import com.dass.niel.hospital.management.system.controllers.PatientController;
import com.dass.niel.hospital.management.system.controllers.StaffController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class HospitalManagementSystemApplicationTests {

	@Autowired
	PatientController patientController;

	@Autowired
	StaffController staffController;

	@Test
	void contextLoads() {
		assertThat(patientController).isNotNull();
		assertThat(staffController).isNotNull();
	}

}
