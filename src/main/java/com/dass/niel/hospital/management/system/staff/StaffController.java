package com.dass.niel.hospital.management.system.staff;

import com.dass.niel.hospital.management.system.patient.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/staff")
public class StaffController {

    @Autowired
    StaffService staffService;

    @GetMapping("/")
    public String staffIndex(Model model){
        model.addAttribute("numStaff", staffService.getNumOfStaff());
        return "staff_index";
    }

    @GetMapping("/new")
    public String staffAdd(){
        return "staff_add";
    }

    @PostMapping(value = "/new", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String staffAddPost(@RequestBody MultiValueMap<String, String> paramMap){
        if(!paramMap.containsKey("firstName") || !paramMap.containsKey("lastName") || !paramMap.containsKey("dob") ||
                !paramMap.containsKey("salary") || !paramMap.containsKey("role") || !paramMap.containsKey("hiredOn")){
            throw new RuntimeException("Insufficient POST parameters provided");
        }

        Staff staff = Staff.builder()
                .firstName(paramMap.getFirst("firstName"))
                .lastName(paramMap.getFirst("lastName"))
                .dob(LocalDate.parse(paramMap.getFirst("dob")))
                .department(paramMap.containsKey("department") && paramMap.getFirst("department").length()>0 ? paramMap.getFirst("department") : null)
                .role(paramMap.getFirst("role"))
                .hiredOn(LocalDate.parse(paramMap.getFirst("hiredOn")))
                .salary(Integer.valueOf(paramMap.getFirst("salary")))
                .build();

        staffService.addNewStaff(staff);

        return "redirect:/staff/";
    }

    @GetMapping("/all")
    @ResponseBody
    public String getAllStaff(){
        List<Staff> staffList = staffService.getAllStaff();
        return staffList.toString();
    }

}
