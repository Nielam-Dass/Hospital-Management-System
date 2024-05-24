package com.dass.niel.hospital.management.system;

import com.dass.niel.hospital.management.system.services.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HospitalManagementSystemApplication implements CommandLineRunner {

	@Autowired
	MedicalRecordService medicalRecordService;

	public static void main(String[] args) {
		SpringApplication.run(HospitalManagementSystemApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		medicalRecordService.init();
	}
}
