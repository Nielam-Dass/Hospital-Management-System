package com.dass.niel.hospital.management.system;

import com.dass.niel.hospital.management.system.patient.Patient;
import com.dass.niel.hospital.management.system.patient.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.List;

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
			Patient patient = Patient.builder()
					.firstName("Adam")
					.lastName("Smith")
					.dob(LocalDate.of(2000, 1, 1))
					.sex("Male")
					.ssn(111223333)
					.phoneNumber("555-555-5555")
					.insurance("XHealth")
					.build();

			patientRepository.save(patient);
			List<Patient> patientList = patientRepository.findAll();
			System.out.println(patientList);
		};

	}

}
