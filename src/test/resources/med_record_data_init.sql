truncate table medical_record;

insert into medical_record (record_id, name, description, created_on, patient_id, added_by, record_file) values
(1, 'Adam Smith vaccine records', 'List of vaccinations given before 2016', '2016-10-18', 2, 5, 'file-store-testing/adams_vax.txt'),
(2, 'Adam Smith Allergy List', 'Patient is allergic to pollen', '2008-08-17', 2, 5, null),
(3, 'James White Heart Checkup', 'Patient heart is healthy', '2008-08-17', 6, 3, null);

alter sequence record_sequence restart with 4;
