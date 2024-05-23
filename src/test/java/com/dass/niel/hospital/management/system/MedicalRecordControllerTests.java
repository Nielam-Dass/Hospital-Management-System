package com.dass.niel.hospital.management.system;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"/patient_schema_init.sql", "/staff_schema_init.sql", "/med_record_schema_init.sql"})
public class MedicalRecordControllerTests {

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void initFiles() throws IOException {
        Path record_file_store = Paths.get("file-store-testing");
        if(!Files.exists(record_file_store)){
            Files.createDirectories(record_file_store);
        }
        for(File f : record_file_store.toFile().listFiles()){
            if(!f.delete()){
                throw new IOException("Could not clear test directory");
            }
        }
        Path example_records = Paths.get("src","test", "resources", "example_records");
        for(File f : example_records.toFile().listFiles()){
            Files.copy(f.toPath(), record_file_store.resolve(f.getName()));
        }

    }

    @Test
    void testMedRecordIndexWithoutDbInit() throws Exception {
        mockMvc.perform(get("/medical-records"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Hospital Medical Records")))
                .andExpect(content().string(containsString("0 medical record(s)")));

        mockMvc.perform(get("/medical-records/"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Hospital Medical Records")))
                .andExpect(content().string(containsString("0 medical record(s)")));
    }

    @Test
    @Sql(scripts = {"/patient_data_init.sql", "/staff_data_init.sql", "/med_record_data_init.sql"})
    void testMedRecordIndexWithDbInit() throws Exception {
        mockMvc.perform(get("/medical-records"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Hospital Medical Records")))
                .andExpect(content().string(containsString("3 medical record(s)")));

        mockMvc.perform(get("/medical-records/"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Hospital Medical Records")))
                .andExpect(content().string(containsString("3 medical record(s)")));
    }

    @Test
    void testMedRecordAddWithoutDbInit() throws Exception {
        mockMvc.perform(get("/medical-records"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("0 medical record(s)")));

        mockMvc.perform(get("/medical-records/new"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Add Medical Record")));

        MockMultipartFile file = new MockMultipartFile("file", "wrist_pain.txt", MediaType.TEXT_PLAIN_VALUE, "Wrist pain described as 6/10".getBytes());

        mockMvc.perform(multipart("/medical-records/new").file(file)
                        .param("name","Wrist injury notes").param("description", "Notes about Lisa White's wrist injury")
                        .param("createdOn", "2024-05-12").param("patientSsn", "555443333")
                        .param("staffId", "5"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/medical-records"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("1 medical record(s)")));
    }

    @Test
    @Sql(scripts = {"/patient_data_init.sql", "/staff_data_init.sql", "/med_record_data_init.sql"})
    void testMedRecordAddWithDbInit() throws Exception {
        mockMvc.perform(get("/medical-records"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("3 medical record(s)")));

        mockMvc.perform(get("/medical-records/new"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Add Medical Record")));

        MockMultipartFile file = new MockMultipartFile("file", "wrist_pain.txt", MediaType.TEXT_PLAIN_VALUE, "Wrist pain described as 6/10".getBytes());

        mockMvc.perform(multipart("/medical-records/new").file(file)
                        .param("name","Wrist injury notes").param("description", "Notes about Lisa White's wrist injury")
                        .param("createdOn", "2024-05-12").param("patientSsn", "555443333")
                        .param("staffId", "5"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/medical-records"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("4 medical record(s)")));
    }

    @Test
    @Sql(scripts = {"/patient_data_init.sql", "/staff_data_init.sql", "/med_record_data_init.sql"})
    void testMedRecordSearch() throws Exception {
        mockMvc.perform(get("/medical-records"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("3 medical record(s)")));

        mockMvc.perform(get("/medical-records/search"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Search Medical Records")));

        mockMvc.perform(post("/medical-records/search").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("ssn", "222334444").param("firstName", "")
                        .param("lastName", "").param("keyword", ""))
                .andExpect(status().isOk()).andExpect(content().string(containsString("2 result(s)")));

        mockMvc.perform(post("/medical-records/search").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("ssn", "").param("firstName", "Adam")
                        .param("lastName", "Smith").param("keyword", ""))
                .andExpect(status().isOk()).andExpect(content().string(containsString("2 result(s)")));

        mockMvc.perform(post("/medical-records/search").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("ssn", "").param("firstName", "Adam")
                        .param("lastName", "Smith").param("keyword", "vaccination"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("1 result(s)")));

        mockMvc.perform(post("/medical-records/search").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("ssn", "444556666").param("firstName", "")
                        .param("lastName", "").param("keyword", "heart"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("1 result(s)")));

        mockMvc.perform(post("/medical-records/search").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("ssn", "444556666").param("firstName", "")
                        .param("lastName", "").param("keyword", "vaccination"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("0 result(s)")));

        mockMvc.perform(post("/medical-records/search").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("ssn", "").param("firstName", "Corey")
                        .param("lastName", "White"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("0 result(s)")));

        mockMvc.perform(get("/medical-records"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("3 medical record(s)")));
    }

    @Test
    void testViewAllMedRecordEmpty() throws Exception {
        mockMvc.perform(get("/medical-records/all"))
                .andExpect(status().isOk()).andExpect(content().string(containsString("[]")));
    }

    @Test
    @Sql(scripts = {"/patient_data_init.sql", "/staff_data_init.sql", "/med_record_data_init.sql"})
    void testViewAllMedRecordNonempty() throws Exception {
        mockMvc.perform(get("/medical-records/all"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("recordIdd=1")))
                .andExpect(content().string(containsString("recordIdd=2")))
                .andExpect(content().string(containsString("recordIdd=3")));
    }

    @Test
    @Sql(scripts = {"/patient_data_init.sql", "/staff_data_init.sql", "/med_record_data_init.sql"})
    void testViewOneRecord() throws Exception {
        mockMvc.perform(get("/medical-records/record/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Adam Smith vaccine records")))
                .andExpect(content().string(containsString("List of vaccinations given before 2016")));

        mockMvc.perform(get("/medical-records/record/2"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Adam Smith Allergy List")));

        mockMvc.perform(get("/medical-records/record/3"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("James White Heart Checkup")));
    }
}
