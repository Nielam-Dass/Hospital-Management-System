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
@Sql(scripts = {"/staff_schema_init.sql"})
public class StaffControllerTests {
    @Autowired
    MockMvc mockMvc;

    @Test
    void testStaffIndexWithoutDbInit() throws Exception {
        mockMvc.perform(get("/staff/"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Staff Home")))
                .andExpect(content().string(containsString("0 staff in database")));
    }

    @Test
    @Sql(scripts = {"/staff_data_init.sql"})
    void testStaffIndexWithDbInit() throws Exception {
        mockMvc.perform(get("/staff/"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Staff Home")))
                .andExpect(content().string(containsString("5 staff in database")));
    }

    @Test
    void testStaffAddWithoutDbInit() throws Exception {
        mockMvc.perform(get("/staff/"))
                .andExpect(content().string(containsString("0 staff in database")));

        mockMvc.perform(post("/staff/new").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "Max").param("lastName", "Anderson")
                        .param("dob", "1979-01-09").param("department", "Cardiology")
                        .param("role", "Heart Surgeon").param("hiredOn", "2015-03-05")
                        .param("salary", "195000"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/staff/"))
                .andExpect(content().string(containsString("1 staff in database")));
    }

    @Test
    @Sql(scripts = {"/staff_data_init.sql"})
    void testStaffAddWithDbInit() throws Exception {
        mockMvc.perform(get("/staff/"))
                .andExpect(content().string(containsString("5 staff in database")));

        mockMvc.perform(get("/staff/new"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Add Staff")));

        mockMvc.perform(post("/staff/new").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "David").param("lastName", "Miller")
                        .param("dob", "1972-07-20").param("department", "Neurology")
                        .param("role", "Neurosurgeon").param("hiredOn", "2018-03-12")
                        .param("salary", "210000"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/staff/"))
                .andExpect(content().string(containsString("6 staff in database")));
    }

    @Test
    @Sql(scripts = {"/staff_data_init.sql"})
    void testStaffSearch() throws Exception {
        mockMvc.perform(get("/staff/"))
                .andExpect(content().string(containsString("5 staff in database")));

        mockMvc.perform(get("/staff/search"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Staff Search")));

        mockMvc.perform(post("/staff/search").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "Ben").param("lastName", "Robinson")
                        .param("department", "Nursing"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("1 result(s)")));

        mockMvc.perform(post("/staff/search").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "").param("lastName", "")
                        .param("department", "Cardiology"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("2 result(s)")));

        mockMvc.perform(post("/staff/search").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "Max").param("lastName", "Anderson")
                        .param("department", "Cardiology"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("1 result(s)")));

        mockMvc.perform(post("/staff/search").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "Daniel").param("lastName", "Carter")
                        .param("department", ""))
                .andExpect(status().isOk()).andExpect(content().string(containsString("1 result(s)")));

        mockMvc.perform(post("/staff/search").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "Daniel").param("lastName", "Carter")
                        .param("department", "Cardiology"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("0 result(s)")));

        mockMvc.perform(post("/staff/search").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "Daniel"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("First and last names must be provided together")));

        mockMvc.perform(get("/staff/"))
                .andExpect(content().string(containsString("5 staff in database")));

    }

    @Test
    void testViewAllStaffEmpty() throws Exception {
        mockMvc.perform(get("/staff/all"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("[]")));
    }

    @Test
    @Sql(scripts = {"/staff_data_init.sql"})
    void testViewAllStaffNonempty() throws Exception {
        mockMvc.perform(get("/staff/all"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("staffId=1")))
                .andExpect(content().string(containsString("staffId=2")))
                .andExpect(content().string(containsString("staffId=3")))
                .andExpect(content().string(containsString("staffId=4")))
                .andExpect(content().string(containsString("staffId=5")));
    }

    @Test
    @Sql(scripts = {"/staff_data_init.sql"})
    void testStaffUpdate() throws Exception {
        mockMvc.perform(get("/staff/"))
                .andExpect(content().string(containsString("5 staff in database")));

        mockMvc.perform(get("/staff/view/4"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Christopher Wilson Employee Profile")))
                .andExpect(content().string(containsString("DOB: 1987-05-15")))
                .andExpect(content().string(containsString("Hired on: 2021-11-07")))
                .andExpect(content().string(containsString("value=\"Cleaning\"")))
                .andExpect(content().string(containsString("value=\"Janitor\"")))
                .andExpect(content().string(containsString("value=\"50000\"")));

        mockMvc.perform(post("/staff/view/4").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "Christopher").param("lastName", "Wilson")
                        .param("department", "Cleaning").param("role", "Senior Janitor")
                        .param("salary", "70000").param("dob", "2000-01-01"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/staff/view/4"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Christopher Wilson Employee Profile")))
                .andExpect(content().string(containsString("DOB: 1987-05-15")))
                .andExpect(content().string(containsString("Hired on: 2021-11-07")))
                .andExpect(content().string(containsString("value=\"Cleaning\"")))
                .andExpect(content().string(containsString("value=\"Senior Janitor\"")))
                .andExpect(content().string(containsString("value=\"70000\"")));

        mockMvc.perform(get("/staff/"))
                .andExpect(content().string(containsString("5 staff in database")));

    }
}
