package com.dass.niel.hospital.management.system.controllers;

import com.dass.niel.hospital.management.system.entities.Patient;
import com.dass.niel.hospital.management.system.entities.Visit;
import com.dass.niel.hospital.management.system.services.PatientService;
import com.dass.niel.hospital.management.system.services.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
@RequestMapping("/visits")
public class VisitController {
    @Autowired
    VisitService visitService;

    @Autowired
    PatientService patientService;

    @GetMapping(value = {"", "/"})
    public String visitIndex(Model model){
        model.addAttribute("numActiveVisits", visitService.getNumOfActiveVisits());
        return "visit/visit_index";
    }

    @GetMapping("/search")
    public String visitSearch(Model model){
        return "visit/visit_search";
    }

    @PostMapping(value = "/search", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String visitSearchPost(@RequestBody MultiValueMap<String, String> paramMap, Model model){
        String firstName = (paramMap.containsKey("firstName") && paramMap.getFirst("firstName").length()>0) ? paramMap.getFirst("firstName") : null;
        String lastName = (paramMap.containsKey("lastName") && paramMap.getFirst("lastName").length()>0) ? paramMap.getFirst("lastName") : null;
        String ssnStr = (paramMap.containsKey("ssn") && paramMap.getFirst("ssn").length()>0) ? paramMap.getFirst("ssn") : null;
        String fromDateStr = (paramMap.containsKey("fromDate") && paramMap.getFirst("fromDate").length()>0) ? paramMap.getFirst("fromDate") : null;
        String toDateStr = (paramMap.containsKey("toDate") && paramMap.getFirst("toDate").length()>0) ? paramMap.getFirst("toDate") : null;

        try{
            Integer ssn = (ssnStr==null) ? null : Integer.valueOf(ssnStr);
            LocalDate fromDate = (fromDateStr==null) ? null : LocalDate.parse(fromDateStr);
            LocalDate toDate = (toDateStr==null) ? null : LocalDate.parse(toDateStr);
            List<Visit> visitList = visitService.getVisitsBySsnNameAndDateRange(ssn, firstName, lastName, fromDate, toDate);
            model.addAttribute("visits", visitList);
            return "visit/visit_search";
        }
        catch(NumberFormatException | DateTimeParseException e){
            return "redirect:/visits/search";
        }

    }

    @GetMapping("/new")
    public String visitAdd(){
        return "visit/visit_add";
    }

    @PostMapping(value = "/new", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String visitAddPost(@RequestBody MultiValueMap<String, String> paramMap){
        String firstName = (paramMap.containsKey("firstName") && paramMap.getFirst("firstName").length()>0) ? paramMap.getFirst("firstName") : null;
        String lastName = (paramMap.containsKey("lastName") && paramMap.getFirst("lastName").length()>0) ? paramMap.getFirst("lastName") : null;
        String sex = (paramMap.containsKey("sex") && paramMap.getFirst("sex").length()>0) ? paramMap.getFirst("sex") : null;
        String phoneNumber = (paramMap.containsKey("phoneNumber") && paramMap.getFirst("phoneNumber").length()>0) ? paramMap.getFirst("phoneNumber") : null;
        String insurance = (paramMap.containsKey("insurance") && paramMap.getFirst("insurance").length()>0) ? paramMap.getFirst("insurance") : null;
        String reason = (paramMap.containsKey("reason") && paramMap.getFirst("reason").length()>0) ? paramMap.getFirst("reason") : null;
        String dobStr = (paramMap.containsKey("dob") && paramMap.getFirst("dob").length()>0) ? paramMap.getFirst("dob") : null;
        String ssnStr = (paramMap.containsKey("ssn") && paramMap.getFirst("ssn").length()>0) ? paramMap.getFirst("ssn") : null;
        String admittedOnStr = (paramMap.containsKey("admittedOn") && paramMap.getFirst("admittedOn").length()>0) ? paramMap.getFirst("admittedOn") : null;
        String isNewPatientStr = (paramMap.containsKey("isNewPatient") && paramMap.getFirst("isNewPatient").length()>0) ? paramMap.getFirst("isNewPatient") : null;

        try{
            LocalDate dob = (dobStr!=null) ? LocalDate.parse(dobStr) : null;
            LocalDate admittedOn = LocalDate.parse(admittedOnStr);
            Integer ssn = Integer.valueOf(ssnStr);
            boolean isNewPatient = (isNewPatientStr!=null) && (isNewPatientStr.equals("true"));

            if(isNewPatient){
                Patient newPatient = Patient.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .dob(dob)
                        .sex(sex)
                        .ssn(ssn)
                        .phoneNumber(phoneNumber)
                        .insurance(insurance)
                        .build();

                Patient savedPatient = patientService.addNewPatient(newPatient);

                Visit newVisit = Visit.builder()
                        .admittedOn(admittedOn)
                        .reason(reason)
                        .patient(savedPatient)
                        .build();

                visitService.addNewVisit(newVisit);
            }
            else{
                Patient incomingPatient = patientService.getPatientBySsn(ssn);

                if((incomingPatient!=null) && (firstName==null || incomingPatient.getFirstName().equals(firstName)) &&
                        (lastName==null || incomingPatient.getLastName().equals(lastName)) &&
                        (dob==null || incomingPatient.getDob().equals(dob)) &&
                        (sex==null || incomingPatient.getSex().equals(sex)) &&
                        (phoneNumber==null || incomingPatient.getPhoneNumber().equals(phoneNumber)) &&
                        (insurance==null || incomingPatient.getInsurance().equals(insurance))){

                    Visit newVisit = Visit.builder()
                            .admittedOn(admittedOn)
                            .reason(reason)
                            .patient(incomingPatient)
                            .build();

                    visitService.addNewVisit(newVisit);
                }
            }
            return "redirect:/visits";
        }
        catch(NumberFormatException | DateTimeParseException e){
            return "redirect:/visits";
        }
    }

    @GetMapping("/{visitIdStr}")
    public String visitView(@PathVariable String visitIdStr, Model model){
        try{
            Long visitId = Long.valueOf(visitIdStr);
            Visit visit = visitService.getVisitById(visitId);
            if(visit==null){
                return "redirect:/visits";
            }
            model.addAttribute("visit", visit);
            return "visit/visit_view";
        }
        catch (NumberFormatException nfe){
            return "redirect:/visits";
        }
    }

    @PostMapping(value="/{visitIdStr}", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String visitEnd(@PathVariable String visitIdStr, @RequestBody MultiValueMap<String, String> paramMap){
        String dischargeDateStr = (paramMap.containsKey("dischargeDate") && paramMap.getFirst("dischargeDate").length()>0) ? paramMap.getFirst("dischargeDate") : null;

        try{
            Long visitId = Long.valueOf(visitIdStr);
            Visit visit = visitService.getVisitById(visitId);
            if(visit!=null){
                LocalDate dischargeDate = LocalDate.parse(dischargeDateStr);
                visitService.endVisit(visit, dischargeDate);
            }
            return "redirect:/visits";
        }
        catch (NumberFormatException | DateTimeParseException e){
            return "redirect:/visits";
        }

    }

    @GetMapping("/all")
    @ResponseBody
    public String getAllVisits(){
        List<Visit> visitList = visitService.getAllVisits();
        return visitList.toString();
    }

}
