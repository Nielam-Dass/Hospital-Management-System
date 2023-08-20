package com.dass.niel.hospital.management.system.patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


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
        return "patient_search";
    }

    @PostMapping(value = "/search", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String patientSearchPost(@RequestBody MultiValueMap<String, String> paramMap, Model model){
        String ssn = paramMap.getFirst("ssn");
        String firstName = paramMap.getFirst("firstName");
        String lastName = paramMap.getFirst("lastName");

        List<Patient> patientList;

        if(ssn!=null && ssn.length()>0){
            Patient patient = patientService.getPatientBySsn(Integer.valueOf(ssn));
            if(firstName!=null && firstName.length()>0 && !firstName.equals(patient.getFirstName())){
                patientList = new ArrayList<>();
            }
            else if(lastName!=null && lastName.length()>0 && !lastName.equals(patient.getLastName())){
                patientList = new ArrayList<>();
            }
            else{
                patientList = (patient!=null) ? List.of(patient) : new ArrayList<>();
            }
        }
        else{
            if(firstName!=null && firstName.length()>0 && lastName!=null && lastName.length()>0){
                patientList = patientService.getPatientsByFullName(firstName, lastName);
            }
            else{
                patientList = new ArrayList<>();
                model.addAttribute("insufficientParams", Boolean.TRUE);
            }
        }
        model.addAttribute("patients", patientList);
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

    @GetMapping("/all")
    @ResponseBody
    public String getAllPatients(){
        List<Patient> patientList = patientService.getAllPatients();
        return patientList.toString();
    }

}
