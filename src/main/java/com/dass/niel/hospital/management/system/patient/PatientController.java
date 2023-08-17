package com.dass.niel.hospital.management.system.patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;


@Controller
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    PatientService patientService;

    @GetMapping("/")
    public String patientIndex(Model model){
        model.addAttribute("numPatients", patientService.getNumOfPatients());
        return "patient_index";
    }

    @GetMapping("/search")
    public String patientSearch(Model model){
        model.addAttribute("patients", patientService.getAllPatients());  // For now, we'll just show all patients
        return "patient_search";
    }

    @GetMapping("/new")  // Will be removed later (should only add patient upon first visit)
    public String patientAdd(){
        return "patient_add";
    }

    @PostMapping(value = "/new", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String patientAddPost(@RequestBody MultiValueMap<String, String> paramMap){
        if(!paramMap.containsKey("firstName") || !paramMap.containsKey("lastName") || !paramMap.containsKey("dob") ||
            !paramMap.containsKey("sex") || !paramMap.containsKey("ssn") || !paramMap.containsKey("phoneNumber")){
            throw new RuntimeException("Insufficient POST parameters provided");
        }

        Patient patient = Patient.builder()
                .firstName(paramMap.getFirst("firstName"))
                .lastName(paramMap.getFirst("lastName"))
                .dob(LocalDate.parse(paramMap.getFirst("dob")))
                .sex(paramMap.getFirst("sex"))
                .ssn(Integer.valueOf(paramMap.getFirst("ssn")))
                .phoneNumber(paramMap.getFirst("phoneNumber"))
                .insurance(paramMap.containsKey("insurance") && paramMap.getFirst("insurance").length()>0 ? paramMap.getFirst("insurance") : null)
                .build();

        patientService.addNewPatient(patient);

        return "redirect:";
    }

}
