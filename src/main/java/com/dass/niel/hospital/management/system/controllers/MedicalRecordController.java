package com.dass.niel.hospital.management.system.controllers;

import com.dass.niel.hospital.management.system.entities.MedicalRecord;
import com.dass.niel.hospital.management.system.entities.Patient;
import com.dass.niel.hospital.management.system.entities.Staff;
import com.dass.niel.hospital.management.system.services.MedicalRecordService;
import com.dass.niel.hospital.management.system.services.PatientService;
import com.dass.niel.hospital.management.system.services.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
@RequestMapping("/medical-records")
public class MedicalRecordController {
    @Autowired
    MedicalRecordService medicalRecordService;

    @Autowired
    PatientService patientService;

    @Autowired
    StaffService staffService;

    @GetMapping(value = {"", "/"})
    public String medRecordIndex(Model model){
        model.addAttribute("numRecords", medicalRecordService.getNumOfMedRecords());
        return "medical_record/medical_record_index";
    }

    @GetMapping("/search")
    public String medRecordSearch(Model model){
        return "medical_record/medical_record_search";
    }

    @PostMapping(value = "/search", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String medRecordSearchPost(@RequestBody MultiValueMap<String, String> paramMap, Model model){
        String firstName = (paramMap.containsKey("firstName") && paramMap.getFirst("firstName").length()>0) ? paramMap.getFirst("firstName") : null;
        String lastName = (paramMap.containsKey("lastName") && paramMap.getFirst("lastName").length()>0) ? paramMap.getFirst("lastName") : null;
        String keyword = (paramMap.containsKey("keyword") && paramMap.getFirst("keyword").length()>0) ? paramMap.getFirst("keyword") : null;
        String ssnStr = (paramMap.containsKey("ssn") && paramMap.getFirst("ssn").length()>0) ? paramMap.getFirst("ssn") : null;

        try{
            Integer ssn = (ssnStr==null) ? null : Integer.valueOf(ssnStr);
            List<MedicalRecord> medicalRecordList = medicalRecordService.getMedRecordsBySsnNameAndKeyword(ssn, firstName, lastName, keyword);
            model.addAttribute("medicalRecords", medicalRecordList);
            return "medical_record/medical_record_search";
        }
        catch (NumberFormatException ne){
            return "redirect:/medical-records/search";
        }

    }

    @GetMapping("/new")
    public String medRecordAdd(Model model){
        model.addAttribute("currentDate", LocalDate.now());
        return "medical_record/medical_record_add";
    }

    @PostMapping(value = "/new")
    public String medRecordAddPost(@RequestParam("name") String nameParam,
                                   @RequestParam("description") String descriptionParam,
                                   @RequestParam("createdOn") String createdOnStParam,
                                   @RequestParam("patientSsn") String patientSsnStrParam,
                                   @RequestParam("staffId") String staffIdStrParam,
                                   @RequestParam("file") MultipartFile file){

        String name = (nameParam!=null && nameParam.length()>0) ? nameParam : null;
        String description = (descriptionParam!=null && descriptionParam.length()>0) ? descriptionParam : null;
        String createdOnStr = (createdOnStParam!=null && createdOnStParam.length()>0) ? createdOnStParam : null;
        String patientSsnStr = (patientSsnStrParam!=null && patientSsnStrParam.length()>0) ? patientSsnStrParam : null;
        String staffIdStr = (staffIdStrParam!=null && staffIdStrParam.length()>0) ? staffIdStrParam : null;

        try{
            LocalDate createdOn = LocalDate.parse(createdOnStr);
            Integer patientSsn = Integer.valueOf(patientSsnStr);
            Long staffId = Long.valueOf(staffIdStr);

            Patient patient = patientService.getPatientBySsn(patientSsn);
            Staff staff = staffService.getStaffById(staffId);

            if(patient!=null && staff!=null){
                MedicalRecord newMedicalRecord = MedicalRecord.builder()
                        .name(name)
                        .description(description)
                        .createdOn(createdOn)
                        .patient(patient)
                        .addedBy(staff)
                        .build();

                medicalRecordService.addNewMedRecord(newMedicalRecord, file);
            }

            return "redirect:/medical-records";
        }
        catch (NumberFormatException | DateTimeParseException e){
            return "redirect:/medical-records";
        }

    }

    @GetMapping("/record/{recordIdStr}")
    public String medRecordView(@PathVariable String recordIdStr, Model model){
        try{
            Long recordId = Long.valueOf(recordIdStr);
            MedicalRecord medicalRecord = medicalRecordService.getMedRecordById(recordId);
            if(medicalRecord==null){
                return "redirect:/medical-records";
            }
            model.addAttribute("medicalRecord", medicalRecord);
            return "medical_record/medical_record_view";
        }
        catch (NumberFormatException nfe){
            return "redirect:/medical-records";
        }
    }

    @GetMapping("/record/{recordIdStr}/download")
    public ResponseEntity<Resource> downloadMedRecordFile(@PathVariable String recordIdStr){
        Long recordId = Long.valueOf(recordIdStr);
        Resource file = medicalRecordService.loadMedRecordFile(recordId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @GetMapping("/all")
    @ResponseBody
    public String getAllMedRecords(){
        List<MedicalRecord> medicalRecordList = medicalRecordService.getAllMedRecords();
        return medicalRecordList.toString();
    }

}
