package com.dass.niel.hospital.management.system.controllers;

import com.dass.niel.hospital.management.system.entities.Patient;
import com.dass.niel.hospital.management.system.services.PatientService;
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

    @GetMapping(value = {"", "/"})
    public String patientIndex(Model model){
        model.addAttribute("numPatients", patientService.getNumOfPatients());
        return "patient/patient_index";
    }

    @GetMapping("/search")
    public String patientSearch(Model model){
        return "patient/patient_search";
    }

    @PostMapping(value = "/search", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String patientSearchPost(@RequestBody MultiValueMap<String, String> paramMap, Model model){
        String ssn = paramMap.getFirst("ssn");
        String firstName = paramMap.getFirst("firstName");
        String lastName = paramMap.getFirst("lastName");

        List<Patient> patientList;

        if(ssn!=null && ssn.length()>0){
            Patient patient = patientService.getPatientBySsn(Integer.valueOf(ssn));
            if(patient==null){
                patientList = new ArrayList<>();
            }
            else if(firstName!=null && firstName.length()>0 && !firstName.equals(patient.getFirstName())){
                patientList = new ArrayList<>();
            }
            else if(lastName!=null && lastName.length()>0 && !lastName.equals(patient.getLastName())){
                patientList = new ArrayList<>();
            }
            else{
                patientList = List.of(patient);
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
        return "patient/patient_search";
    }

    @GetMapping("new")  // Will be removed later (should only add patient upon first visit)
    public String patientAdd(){
        return "patient/patient_add";
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

        return "redirect:/patient";
    }

    @GetMapping("/view/{patientIdStr}")
    public String patientView(@PathVariable String patientIdStr, Model model){
        try{
            Long patientId = Long.valueOf(patientIdStr);
            Patient patient = patientService.getPatientById(patientId);
            if(patient==null){
                return "redirect:/patient";
            }
            model.addAttribute("patient", patient);
            return "patient/patient_view";
        }
        catch (NumberFormatException nfe) {
            return "redirect:/patient";
        }
    }

    @PostMapping(value="/view/{patientIdStr}", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String patientUpdate(@PathVariable String patientIdStr, @RequestBody MultiValueMap<String, String> paramMap){
        if(!paramMap.containsKey("firstName") || !paramMap.containsKey("lastName")
                || !paramMap.containsKey("ssn") || !paramMap.containsKey("phoneNumber") || !paramMap.containsKey("insurance")){
            throw new RuntimeException("Insufficient POST parameters provided");
        }
        try{
            Long patientId = Long.valueOf(patientIdStr);
            Patient patient = patientService.getPatientById(patientId);
            if(patient!=null){
                patient.setFirstName(paramMap.getFirst("firstName"));
                patient.setLastName(paramMap.getFirst("lastName"));
                patient.setSsn(Integer.valueOf(paramMap.getFirst("ssn")));
                patient.setPhoneNumber(paramMap.getFirst("phoneNumber"));
                patient.setInsurance(paramMap.containsKey("insurance") && paramMap.getFirst("insurance").length()>0 ? paramMap.getFirst("insurance") : null);
                patientService.updatePatient(patient);
            }
            return "redirect:/patient";
        }
        catch (NumberFormatException nfe) {
            return "redirect:/patient";
        }

    }

    @GetMapping("/all")
    @ResponseBody
    public String getAllPatients(){
        List<Patient> patientList = patientService.getAllPatients();
        return patientList.toString();
    }

}
