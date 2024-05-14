package com.dass.niel.hospital.management.system.controllers;

import com.dass.niel.hospital.management.system.services.StaffService;
import com.dass.niel.hospital.management.system.entities.Staff;
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
@RequestMapping("/staff")
public class StaffController {

    @Autowired
    StaffService staffService;

    @GetMapping(value = {"", "/"})
    public String staffIndex(Model model){
        model.addAttribute("numStaff", staffService.getNumOfStaff());
        return "staff/staff_index";
    }

    @GetMapping("/search")
    public String staffSearch(Model model){
        return "staff/staff_search";
    }

    @PostMapping(value = "/search", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String staffSearchPost(@RequestBody MultiValueMap<String, String> paramMap, Model model){
        String firstName = paramMap.getFirst("firstName");
        String lastName = paramMap.getFirst("lastName");
        String department = paramMap.getFirst("department");
        boolean fullNameProvided = firstName!=null && firstName.length()>0 && lastName!=null && lastName.length()>0;
        boolean noNameProvided = (firstName==null || firstName.length()==0) && (lastName==null || lastName.length()==0);
        boolean departmentProvided = department!=null && department.length()>0;

        List<Staff> staffList;

        if(departmentProvided){
            if(fullNameProvided){
                staffList = staffService.getStaffByFullNameAndDepartment(firstName, lastName, department);
            }
            else if(noNameProvided){
                staffList = staffService.getStaffInDepartment(department);
            }
            else{
                staffList = new ArrayList<>();
                model.addAttribute("insufficientParams", Boolean.TRUE);
            }

        }
        else if(fullNameProvided) {
            staffList = staffService.getStaffByFullName(firstName, lastName);
        }
        else{
            staffList = new ArrayList<>();
            model.addAttribute("insufficientParams", Boolean.TRUE);
        }

        model.addAttribute("staff", staffList);
        return "staff/staff_search";
    }

    @GetMapping("/new")
    public String staffAdd(){
        return "staff/staff_add";
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

        return "redirect:/staff";
    }

    @GetMapping("/view/{staffIdStr}")
    public String staffView(@PathVariable String staffIdStr, Model model){
        try{
            Long staffId = Long.valueOf(staffIdStr);
            Staff staff = staffService.getStaffById(staffId);
            if(staff==null){
                return "redirect:/staff";
            }
            model.addAttribute("staff", staff);
            return "staff/staff_view";
        }
        catch (NumberFormatException nfe) {
            return "redirect:/staff";
        }
    }

    @PostMapping(value = "/view/{staffIdStr}", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String staffUpdate(@PathVariable String staffIdStr, @RequestBody MultiValueMap<String, String> paramMap){
        if(!paramMap.containsKey("firstName") || !paramMap.containsKey("lastName")
                || !paramMap.containsKey("department") || !paramMap.containsKey("role")
                || !paramMap.containsKey("salary")){
            throw new RuntimeException("Insufficient POST parameters provided");
        }

        try{
            Long staffId = Long.valueOf(staffIdStr);
            Staff staff = staffService.getStaffById(staffId);
            if(staff!=null){
                staff.setFirstName(paramMap.getFirst("firstName"));
                staff.setLastName(paramMap.getFirst("lastName"));
                staff.setDepartment(paramMap.getFirst("department"));
                staff.setRole(paramMap.getFirst("role"));
                staff.setSalary(Integer.valueOf(paramMap.getFirst("salary")));
                staffService.updateStaff(staff);
            }
            return "redirect:/staff";
        }
        catch (NumberFormatException nfe){
            return "redirect:/staff";
        }

    }

    @GetMapping("/all")
    @ResponseBody
    public String getAllStaff(){
        List<Staff> staffList = staffService.getAllStaff();
        return staffList.toString();
    }

}
