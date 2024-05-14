package com.dass.niel.hospital.management.system;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"/patient_schema_init.sql"})
public class PatientControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void testPatientIndexWithoutDbInit() throws Exception {
        mockMvc.perform(get("/patient/"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Patient Home")))
                .andExpect(content().string(containsString("0 patient(s) in database")));

        mockMvc.perform(get("/patient"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Patient Home")))
                .andExpect(content().string(containsString("0 patient(s) in database")));
    }

    @Test
    @Sql(scripts = {"/patient_data_init.sql"})
    void testPatientIndexWithDbInit() throws Exception {
        mockMvc.perform(get("/patient/"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Patient Home")))
                .andExpect(content().string(containsString("7 patient(s) in database")));

        mockMvc.perform(get("/patient"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Patient Home")))
                .andExpect(content().string(containsString("7 patient(s) in database")));
    }

    @Test
    void testPatientAddWithoutDbInit() throws Exception {
        mockMvc.perform(get("/patient"))
                .andExpect(content().string(containsString("0 patient(s) in database")));

        mockMvc.perform(get("/patient/new"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Add Patient")));

        mockMvc.perform(post("/patient/new").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", "Adam").param("lastName", "Smith")
                .param("dob", "1998-08-04").param("sex", "Male")
                .param("ssn", "111223333").param("phoneNumber", "555-555-5555")
                .param("insurance", "XHealth"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/patient")).andDo(print())
                .andExpect(status().isOk()).andExpect(content().string(containsString("Patient Home")))
                .andExpect(content().string(containsString("1 patient(s) in database")));

        mockMvc.perform(post("/patient/new").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "Adam").param("lastName", "Smith")
                        .param("dob", "2004-04-18").param("sex", "Male")
                        .param("ssn", "222334444").param("phoneNumber", "222-222-2222")
                        .param("insurance", ""))
                        .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/patient")).andDo(print())
                .andExpect(status().isOk()).andExpect(content().string(containsString("Patient Home")))
                .andExpect(content().string(containsString("2 patient(s) in database")));
    }

    @Test
    @Sql(scripts = {"/patient_data_init.sql"})
    void testPatientAddWithDbInit() throws Exception {
        mockMvc.perform(get("/patient"))
                .andExpect(content().string(containsString("7 patient(s) in database")));


        mockMvc.perform(post("/patient/new").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "John").param("lastName", "Smith")
                        .param("dob", "1952-02-08").param("sex", "Male")
                        .param("ssn", "666778888").param("phoneNumber", "400-500-6000")
                        .param("insurance", "Medicare"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/patient"))
                .andExpect(content().string(containsString("8 patient(s) in database")));
    }

    @Test
    @Sql(scripts = {"/patient_data_init.sql"})
    void testPatientSearch() throws Exception {
        mockMvc.perform(get("/patient"))
                .andExpect(content().string(containsString("7 patient(s) in database")));

        mockMvc.perform(get("/patient/search"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Patient Search")));

        mockMvc.perform(post("/patient/search").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("ssn", "333221111")
                        .param("firstName", "").param("lastName", ""))
                .andExpect(status().isOk()).andExpect(content().string(containsString("1 result(s)")));

        mockMvc.perform(post("/patient/search").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("ssn", "")
                        .param("firstName", "Adam").param("lastName", "Smith"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("2 result(s)")));

        mockMvc.perform(post("/patient/search").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("ssn", "222334444")
                        .param("firstName", "Adam").param("lastName", "Smith"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("1 result(s)")));

        mockMvc.perform(post("/patient/search").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("ssn", "000000000")
                        .param("firstName", "Adam").param("lastName", "Smith"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("0 result(s)")));

        mockMvc.perform(post("/patient/search").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "Andrew").param("lastName", "Jackson"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("0 result(s)")));

        mockMvc.perform(post("/patient/search").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("ssn", "111223333")
                        .param("lastName", "Smith"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("1 result(s)")));

        mockMvc.perform(post("/patient/search").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("ssn", "111223333")
                        .param("firstName", "John").param("lastName", "Smith"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("0 result(s)")));

        mockMvc.perform(post("/patient/search").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("lastName", "Smith"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Full name must be provided if SSN is not given")));
    }

    @Test
    void testViewAllPatientsEmpty() throws Exception {
        mockMvc.perform(get("/patient/all"))
                .andExpect(content().string(containsString("[]")));
    }

    @Test
    @Sql(scripts = {"/patient_data_init.sql"})
    void testViewAllPatientsNonempty() throws Exception {
        mockMvc.perform(get("/patient/all"))
                .andExpect(content().string(containsString("patientId=1")))
                .andExpect(content().string(containsString("patientId=2")))
                .andExpect(content().string(containsString("patientId=3")))
                .andExpect(content().string(containsString("patientId=4")))
                .andExpect(content().string(containsString("patientId=5")))
                .andExpect(content().string(containsString("patientId=6")))
                .andExpect(content().string(containsString("patientId=7")));
    }

    @Test
    @Sql(scripts = {"/patient_data_init.sql"})
    void testPatientUpdate() throws Exception {
        mockMvc.perform(get("/patient"))
                .andExpect(content().string(containsString("7 patient(s) in database")));

        mockMvc.perform(get("/patient/profile/2/view"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Adam Smith Patient Profile")))
                .andExpect(content().string(containsString("DOB: 2004-04-18")))
                .andExpect(content().string(containsString("SSN: 222334444")))
                .andExpect(content().string(containsString("Phone number: 222-222-2222")));

        mockMvc.perform(get("/patient/profile/2/edit"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Update Adam Smith patient information")))
                .andExpect(content().string(containsString("DOB: 2004-04-18")))
                .andExpect(content().string(containsString("value=\"222334444\"")))
                .andExpect(content().string(containsString("value=\"222-222-2222\"")));

        mockMvc.perform(post("/patient/profile/2/edit").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "William").param("lastName", "Smith")
                        .param("ssn", "222334444").param("phoneNumber", "222-222-3333")
                        .param("insurance", "XHealth").param("dob", "2004-01-01"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/patient/profile/2/view"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("William Smith Patient Profile")))
                .andExpect(content().string(containsString("DOB: 2004-04-18")))
                .andExpect(content().string(containsString("SSN: 222334444")))
                .andExpect(content().string(containsString("Phone number: 222-222-3333")));

        mockMvc.perform(get("/patient"))
                .andExpect(content().string(containsString("7 patient(s) in database")));
    }

}
