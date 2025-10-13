package com.dass.niel.hospital.management.system;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class RootControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void testRootPage() throws Exception{
        mockMvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(content().string(containsString("Welcome to the Hospital Management System")))
                .andExpect(content().string(containsString("Patient Dashboard")))
                .andExpect(content().string(containsString("href=\"/patient\"")))
                .andExpect(content().string(containsString("Staff Dashboard")))
                .andExpect(content().string(containsString("href=\"/staff\"")))
                .andExpect(content().string(containsString("Visits Dashboard")))
                .andExpect(content().string(containsString("href=\"/visits\"")))
                .andExpect(content().string(containsString("Medical Records Dashboard")))
                .andExpect(content().string(containsString("href=\"/medical-records\"")));
    }
}
