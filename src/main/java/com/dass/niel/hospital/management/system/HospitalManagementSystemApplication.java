package com.dass.niel.hospital.management.system;

import com.dass.niel.hospital.management.system.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HospitalManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(HospitalManagementSystemApplication.class, args);
	}

	@Autowired
	PatientRepository patientRepository;

	@Bean
	CommandLineRunner commandLineRunner(){
		return args -> {

		};

	}

}
