package com.dass.niel.hospital.management.system;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"/patient_schema_init.sql", "/visit_schema_init.sql"})
public class VisitControllerTests {
    @Autowired
    MockMvc mockMvc;

    @Test
    void testVisitIndexWithoutDbInit() throws Exception {
        mockMvc.perform(get("/visits"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Hospital Visit Records")))
                .andExpect(content().string(containsString("0 active visit(s)")));

        mockMvc.perform(get("/visits/"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Hospital Visit Records")))
                .andExpect(content().string(containsString("0 active visit(s)")));
    }

    @Test
    @Sql(scripts = {"/patient_data_init.sql", "/visit_data_init.sql"})
    void testVisitIndexWithDbInit() throws Exception {
        mockMvc.perform(get("/visits"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Hospital Visit Records")))
                .andExpect(content().string(containsString("3 active visit(s)")));

        mockMvc.perform(get("/visits/"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Hospital Visit Records")))
                .andExpect(content().string(containsString("3 active visit(s)")));
    }

    @Test
    void testVisitAddWithoutDbInit() throws Exception {
        mockMvc.perform(get("/visits"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Hospital Visit Records")))
                .andExpect(content().string(containsString("0 active visit(s)")));

        mockMvc.perform(get("/patient"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("0 patient(s) in database")));

        mockMvc.perform(get("/visits/new"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Create new visit")));

        mockMvc.perform(post("/visits/new").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "Billy").param("lastName", "Jackson")
                        .param("dob", "1992").param("sex", "Male")
                        .param("ssn", "102003000").param("phoneNumber", "123-456-7890")
                        .param("insurance", "A-Health").param("isNewPatient", "true")
                        .param("admittedOn", "2024-05-10").param("reason", "Coughing and chest pain"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/visits"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Hospital Visit Records")))
                .andExpect(content().string(containsString("1 active visit(s)")));

        mockMvc.perform(get("/patient"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("1 patient(s) in database")));
    }

    @Test
    @Sql(scripts = {"/patient_data_init.sql", "/visit_data_init.sql"})
    void testVisitAddWithDbInit() throws Exception {
        mockMvc.perform(get("/visits"))
                .andExpect(content().string(containsString("3 active visit(s)")));

        mockMvc.perform(get("/patient"))
                .andExpect(content().string(containsString("7 patient(s) in database")));

        mockMvc.perform(get("/visits/new"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Create new visit")));

        mockMvc.perform(post("/visits/new").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "Billy").param("lastName", "Jackson")
                        .param("dob", "1992").param("sex", "Male")
                        .param("ssn", "102003000").param("phoneNumber", "123-456-7890")
                        .param("insurance", "A-Health").param("isNewPatient", "true")
                        .param("admittedOn", "2024-05-10").param("reason", "Coughing and chest pain"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/visits"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Hospital Visit Records")))
                .andExpect(content().string(containsString("4 active visit(s)")));

        mockMvc.perform(get("/patient"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("8 patient(s) in database")));

        mockMvc.perform(post("/visits/new").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("ssn", "444556666").param("isNewPatient", "false")
                        .param("admittedOn", "2024-05-12").param("reason", "Sprained ankle"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/visits"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Hospital Visit Records")))
                .andExpect(content().string(containsString("5 active visit(s)")));

        mockMvc.perform(get("/patient"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("8 patient(s) in database")));
    }

    @Test
    @Sql(scripts = {"/patient_data_init.sql", "/visit_data_init.sql"})
    void testVisitEnd() throws Exception {
        mockMvc.perform(get("/visits"))
                .andExpect(content().string(containsString("3 active visit(s)")));

        mockMvc.perform(get("/visits/2"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Visit from Lisa White")))
                .andExpect(content().string(containsString("Status: Ongoing")))
                .andExpect(content().string(containsString("Admitted on: 2024-05-12")))
                .andExpect(content().string(containsString("Discharged on: N/A")));

        mockMvc.perform(post("/visits/2").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("dischargeDate", "2024-05-14"))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(get("/visits/2"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Visit from Lisa White")))
                .andExpect(content().string(containsString("Status: Completed")))
                .andExpect(content().string(containsString("Admitted on: 2024-05-12")))
                .andExpect(content().string(containsString("Discharged on: 2024-05-14")));

        mockMvc.perform(get("/visits"))
                .andExpect(content().string(containsString("2 active visit(s)")));
    }

    @Test
    @Sql(scripts = {"/patient_data_init.sql", "/visit_data_init.sql"})
    void testVisitRecordSearch() throws Exception {
        mockMvc.perform(get("/visits"))
                .andExpect(content().string(containsString("3 active visit(s)")));

        mockMvc.perform(get("/visits/search"))
                .andExpect(content().string(containsString("Search patient visits")));

        mockMvc.perform(post("/visits/search").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("ssn", "555443333").param("firstName", "")
                        .param("lastName", "").param("fromDate", "")
                        .param("toDate", ""))
                .andExpect(status().isOk()).andExpect(content().string(containsString("2 result(s)")));

        mockMvc.perform(post("/visits/search").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("ssn", "").param("firstName", "Adam")
                        .param("lastName", "Smith").param("fromDate", "")
                        .param("toDate", ""))
                .andExpect(status().isOk()).andExpect(content().string(containsString("2 result(s)")));

        mockMvc.perform(post("/visits/search").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("ssn", "").param("firstName", "Lisa")
                        .param("lastName", "White").param("fromDate", "2024-05-01")
                        .param("toDate", ""))
                .andExpect(status().isOk()).andExpect(content().string(containsString("1 result(s)")));

        mockMvc.perform(post("/visits/search").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("ssn", "").param("firstName", "")
                        .param("lastName", "").param("fromDate", "")
                        .param("toDate", "2020-01-01"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("1 result(s)")));

        mockMvc.perform(post("/visits/search").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("ssn", "").param("firstName", "")
                        .param("lastName", "").param("fromDate", "2000-01-01")
                        .param("toDate", "2024-05-12"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("3 result(s)")));

        mockMvc.perform(post("/visits/search").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("ssn", "555443333").param("firstName", "Lisa")
                        .param("lastName", "White").param("fromDate", "2000-01-01")
                        .param("toDate", "2024-05-12"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("1 result(s)")));

        mockMvc.perform(post("/visits/search").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("ssn", "333445555").param("firstName", "Corey")
                        .param("lastName", "White"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("0 result(s)")));

        mockMvc.perform(get("/visits"))
                .andExpect(content().string(containsString("3 active visit(s)")));

    }

    @Test
    void testViewAllVisitRecordsEmpty() throws Exception {
        mockMvc.perform(get("/visits/all"))
                .andExpect(content().string(containsString("[]")));
    }

    @Test
    @Sql(scripts = {"/patient_data_init.sql", "/visit_data_init.sql"})
    void testViewAllVisitRecordsNonempty() throws Exception {
        mockMvc.perform(get("/visits/all"))
                .andExpect(content().string(containsString("visitId=1")))
                .andExpect(content().string(containsString("visitId=2")))
                .andExpect(content().string(containsString("visitId=3")))
                .andExpect(content().string(containsString("visitId=4")));
    }
}
